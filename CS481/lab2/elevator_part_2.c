/* elevator_null.c
   Null solution for the elevator threads lab.
   Jim Plank
   CS560
   Lab 2
   January, 2009
 */

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include "dllist.h"
#include "elevator.h"

Dllist *floors;
pthread_mutex_t *floor_locks;

void add_to_list_sorted_to(Dllist people_list, Person *p)
{
	Dllist next = dll_first(people_list);

	while(jval_v(dll_val(dll_next(next))) && ((Person*)jval_v(dll_val(dll_next(next))))->to < p->to)
		next = dll_next(next);

	dll_insert_b(people_list, new_jval_v((void*)p));
}

void add_to_list_sorted_from(Dllist people_list, Person *p)
{
	Dllist next = dll_first(people_list);

	while(dll_next(next) && ((Person*)jval_v(dll_val(dll_next(next))))->from < p->from)
		next = dll_next(next);

	dll_insert_b(people_list, new_jval_v((void*)p));
}

void initialize_simulation(Elevator_Simulation *es)
{
	int x;
	floors = (Dllist*)malloc(sizeof(Dllist) * es->nfloors);
	for(x = 0; x < es->nfloors; x++)
		floors[x] = new_dllist();
	floor_locks = (pthread_mutex_t*)malloc(sizeof(pthread_mutex_t) * es->nfloors);
}

void initialize_elevator(Elevator *e)
{
}

void initialize_person(Person *e)
{
}

void wait_for_elevator(Person *p)
{
	int floor = p->from - 1;
	pthread_mutex_t *lock = floor_locks + floor;
	pthread_mutex_lock(lock);
	dll_append(floors[floor], new_jval_v(p));
	pthread_mutex_unlock(lock);
	pthread_cond_wait(p->cond, p->lock);
}

void wait_to_get_off_elevator(Person *p)
{
	while(p->e->onfloor != p->to)
	{
		pthread_cond_wait(p->cond, p->lock);
		pthread_mutex_unlock(p->lock);
	}

	pthread_mutex_lock(p->e->lock);
	pthread_mutex_unlock(p->e->lock);
}

void person_done(Person *p)
{
	pthread_mutex_unlock(p->e->lock);
	pthread_cond_signal(p->e->cond);
}

void *elevator(void *arg)
{
	Person *next_person;
	Dllist next, delete;
	Elevator *current_elevator = (Elevator*)arg;
	int direction = 1;
	int floor;
	static int openDoor = 0;
	while(1)
	{
		if(current_elevator->onfloor + direction > current_elevator->es->nfloors || current_elevator->onfloor + direction == 0)
			direction *= -1;

		move_to_floor(current_elevator, current_elevator->onfloor + direction);
		floor = current_elevator->onfloor - 1;

		if(jval_v(dll_val(dll_first(current_elevator->people))))
		{
			//Remove everyone from the elevator
			for(next = dll_first(current_elevator->people); jval_v(dll_val(next)); next = dll_next(next))
			{
				next_person = (Person*)jval_v(dll_val(next));
				if(!openDoor && current_elevator->onfloor == next_person->to)
				{
					open_door(current_elevator);
					openDoor = 1;
					pthread_cond_signal(((Person*)jval_v(dll_val(next)))->cond);
				}
			}
		}

		if(jval_v(dll_val(dll_first(floors[floor]))))
		{
			pthread_mutex_lock(floor_locks + floor);
			for(next = dll_first(floors[floor]); jval_v(dll_val(next)); next = dll_next(next))
			{
				next_person = (Person*)jval_v(dll_val(next));
				if((direction > 0 && next_person->to > current_elevator->onfloor) || 
						(direction < 0 && next_person->to < current_elevator->onfloor))
				{
					if(!openDoor)
					{
						open_door(current_elevator);
						openDoor = 1;
					}
					next_person->e = current_elevator;
					pthread_cond_signal(next_person->cond);
				}
			}
			pthread_mutex_unlock(floor_locks + floor);
		}
		else
			printf("");

		if(openDoor)
		{
			close_door(current_elevator);
			openDoor = 0;
		}
	}
  return NULL;
}
