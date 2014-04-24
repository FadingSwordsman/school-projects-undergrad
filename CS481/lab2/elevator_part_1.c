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

Dllist global;
pthread_mutex_t *list_lock;

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
	global = new_dllist();
	list_lock = (pthread_mutex_t*)malloc(sizeof(pthread_mutex_t));
}

void initialize_elevator(Elevator *e)
{
}

void initialize_person(Person *e)
{
}

void wait_for_elevator(Person *p)
{
	Jval personVal = new_jval_v(p);
	pthread_mutex_lock(list_lock);
	dll_append(global, personVal);
	pthread_mutex_unlock(list_lock);
	pthread_cond_wait(p->cond, p->lock);
	pthread_mutex_unlock(p->e->lock);
	pthread_cond_signal(p->e->cond);
}

void wait_to_get_off_elevator(Person *p)
{
	pthread_cond_wait(p->cond, p->lock);
}

void person_done(Person *p)
{
	pthread_mutex_unlock(p->e->lock);
	pthread_cond_signal(p->e->cond);
}

int find_closest_person(int floor_num)
{
	Dllist next = dll_first(global);
}

void *elevator(void *arg)
{
	Person *next_person;
	Dllist next;
	Elevator *current_elevator = (Elevator*)arg;
	while(1)
	{
		next_person = (Person*)NULL;
		while(!next_person)
		{
			pthread_mutex_lock(list_lock);
			next = dll_first(global);
			if(jval_v(dll_val(next)))
			{
				next_person = (Person*)jval_v(dll_val(next));
				dll_delete_node(next);
			}
			pthread_mutex_unlock(list_lock);
		}
		move_to_floor(current_elevator, next_person->from);
		open_door(current_elevator);
		next_person->e = current_elevator;
		pthread_cond_signal(next_person->cond);
		pthread_cond_wait(current_elevator->cond, current_elevator->lock);
		pthread_mutex_unlock(current_elevator->lock);
		close_door(current_elevator);
		move_to_floor(current_elevator, next_person->to);
		open_door(current_elevator);
		pthread_mutex_unlock(next_person->lock);
		pthread_cond_signal(next_person->cond);
		pthread_cond_wait(current_elevator->cond, current_elevator->lock);
		pthread_mutex_unlock(current_elevator->lock);
		close_door(current_elevator);
	}
  return NULL;
}
