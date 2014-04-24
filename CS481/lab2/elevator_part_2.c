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

void wait_for_signal(pthread_cond_t *cond, pthread_mutex_t *lock)
{
	pthread_mutex_lock(lock);
	pthread_cond_wait(cond, lock);
	pthread_mutex_unlock(lock);
}

void signal__wait(pthread_cond_t *cond, pthread_mutex_t *lock)
{
	pthread_mutex_lock(lock);
	pthread_cond_signal(cond);
	pthread_cond_wait(cond, lock);
	pthread_mutex_unlock(lock);
}

void signal_and_wait(pthread_cond_t *cond, pthread_mutex_t *lock, char* string)
{
	pthread_mutex_lock(lock);
	pthread_cond_signal(cond);
	pthread_cond_wait(cond, lock);
	pthread_mutex_unlock(lock);
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
	while(!p->e || p->e->onfloor != p->from)
	{
		signal__wait(p->cond, p->lock);
	}
}

void wait_to_get_off_elevator(Person *p)
{
	while(p->e->onfloor != p->to)
	{
		pthread_mutex_lock(p->lock);
		pthread_cond_signal(p->cond);
		pthread_cond_wait(p->cond, p->lock);
		pthread_cond_signal(p->cond);
		pthread_mutex_unlock(p->lock);
	}
}

void person_done(Person *p)
{
	pthread_mutex_lock(p->lock);
	pthread_cond_signal(p->cond);
	pthread_mutex_unlock(p->lock);
	printf("Exit done\n");
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
		pthread_mutex_lock(floor_locks + floor);

		if(jval_v(dll_val(dll_first(current_elevator->people))))
		{
			delete = dll_nil(current_elevator->people);
			//Remove everyone from the elevator
			for(next = dll_first(current_elevator->people); jval_v(dll_val(next)); next = dll_next(next))
			{
				next_person = (Person*)jval_v(dll_val(next));
				if(delete != dll_nil(current_elevator->people))
				{
					dll_delete_node(delete);
					delete = dll_nil(current_elevator->people);
				}
				if(!openDoor && (current_elevator->onfloor == next_person->to))
				{
					delete = next;
					open_door(current_elevator);
					openDoor = 1;
					signal_and_wait(next_person->cond, next_person->lock, "Elevator off");
					printf("Remove node\n");
				}
				printf("Next\n");
			}
		}
					printf("Load\n");

		if(jval_v(dll_val(dll_first(floors[floor]))))
		{
			for(next = dll_first(floors[floor]); jval_v(dll_val(next)); next = dll_next(next))
			{
				next_person = (Person*)jval_v(dll_val(next));
				if(((direction > 0 && next_person->to > current_elevator->onfloor) || 
						(direction < 0 && next_person->to < current_elevator->onfloor)) && next_person->from == current_elevator->onfloor)
				{
					if(!openDoor)
					{
						open_door(current_elevator);
						openDoor = 1;
					}
					next_person->e = current_elevator;
					pthread_cond_signal(next_person->cond);
					pthread_cond_wait(next_person->cond, next_person->lock);
					pthread_mutex_unlock(next_person->lock);
				}
			}
		}

		if(openDoor)
		{
			close_door(current_elevator);
			openDoor = 0;
		}
		pthread_mutex_unlock(floor_locks + floor);
	}
  return NULL;
}
