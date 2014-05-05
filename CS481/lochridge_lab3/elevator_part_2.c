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
int *elevator_on_floor;
pthread_mutexattr_t attr;

void wait_for_signal(pthread_cond_t *cond, pthread_mutex_t *lock)
{
	pthread_mutex_lock(lock);
	pthread_cond_wait(cond, lock);
	pthread_mutex_unlock(lock);
}

void signal_and_wait(pthread_cond_t *cond, pthread_mutex_t *lock)
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

	pthread_mutex_lock(floor_locks+floor);
	dll_insert_b(floors[floor], new_jval_v(p));
	pthread_mutex_unlock(floor_locks+floor);
	pthread_mutex_lock(p->lock);

	while(!p->e)
	{
		pthread_cond_wait(p->cond, p->lock);
	}
	pthread_mutex_lock(p->e->lock);
	pthread_mutex_unlock(p->lock);
	pthread_cond_signal(p->e->cond);
	pthread_mutex_unlock(p->e->lock);
}

void wait_to_get_off_elevator(Person *p)
{
	pthread_mutex_lock(p->lock);
	while(p->to != p->e->onfloor)
	{
		pthread_cond_wait(p->cond, p->lock);
	}
	
}

void person_done(Person *p)
{
	pthread_mutex_lock(p->e->lock);
	pthread_cond_signal(p->e->cond);
	pthread_mutex_unlock(p->e->lock);
}

int get_direction(Elevator *e, int dir)
{
	if(e->onfloor + dir == 0 || e->onfloor + dir > e->es->nfloors)
		return -dir;
	return dir;
}

void try_close_door(Elevator *e)
{
	if(e->door_open)
		close_door(e);
}

void try_open_door(Elevator *e)
{
	if(!e->door_open)
		open_door(e);
}

void move_elevator_to_floor(Elevator *e, int dir)
{
	try_close_door(e);
	move_to_floor(e, e->onfloor + dir);
}

void unload_to_floor(Elevator *e)
{
	Person *p;
	Dllist node, list;
	list = e->people;
	pthread_mutex_lock(e->lock);
	dll_traverse(node, list)
	{
		p = (Person*)jval_v(dll_val(node));
		if(p->to == e->onfloor)
		{
			pthread_mutex_unlock(e->lock);
			try_open_door(e);
			node = dll_prev(node);
			pthread_mutex_lock(p->lock);
			pthread_cond_signal(p->cond);
			pthread_mutex_lock(e->lock);
			pthread_mutex_unlock(p->lock);
			pthread_cond_wait(e->cond, e->lock);
			pthread_mutex_unlock(e->lock);
		}
	}
	pthread_mutex_unlock(e->lock);
}

int same_direction(int dir, Person *p)
{
	return dir * (p->to - p->from) > 0;
}

void load_from_floor(Elevator *e, int dir)
{
	int floor = e->onfloor - 1;
	Dllist node, list;
	Person *p;
	pthread_mutex_lock(e->lock);
	pthread_mutex_lock(&floor_locks[floor]);
		pthread_mutex_unlock(e->lock);
		list = floors[floor];
		dll_traverse(node, list)
		{
			p = (Person*)jval_v(dll_val(node));
			if(same_direction(dir, p))
			{
			pthread_mutex_lock(p->lock);
			p->e = e;
			try_open_door(e);
			pthread_cond_signal(p->cond);
			pthread_mutex_lock(e->lock);
			pthread_mutex_unlock(p->lock);
			pthread_cond_wait(e->cond, e->lock);
			pthread_mutex_unlock(e->lock);
			node = dll_prev(node);
			dll_delete_node(dll_next(node));
			}
		}
		pthread_mutex_unlock(floor_locks+floor);
}

void *elevator(void *arg)
{
	Elevator *e = (Elevator*)arg;
	int direction = 1;

	while(1)
	{
		direction = get_direction(e, direction);
		move_elevator_to_floor(e, direction);
		unload_to_floor(e);
		load_from_floor(e, direction);
	}
  return NULL;
}
