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
int *updated_floor_count;
pthread_mutex_t *floor_locks;

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
	updated_floor_count = (int*)malloc(sizeof(int)*es->nfloors);
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
	updated_floor_count[floor]++;
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
	if(!p->e->door_open)
	{
		pthread_mutex_lock(p->e->lock);
		pthread_cond_wait(p->e->cond, p->e->lock);
		pthread_mutex_unlock(p->e->lock);
	}
}

void wait_to_get_off_elevator(Person *p)
{
	pthread_mutex_lock(p->lock);
	while(p->to != p->e->onfloor)
		pthread_cond_wait(p->cond, p->lock);
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
	{
		open_door(e);
		pthread_mutex_lock(e->lock);
		pthread_cond_signal(e->cond);
		pthread_mutex_unlock(e->lock);
	}
}

//Find the best marginal floor
int find_next_floor(Elevator *e)
{
	Person *p;
	Dllist node;
	int floor, floor_diff, next = 0;
	int count = 0, nextCount = 0, tempCount;
	for(floor = 0; floor < e->es->nfloors; floor++)
	{
		count = 0;
		pthread_mutex_lock(floor_locks+floor);
		count += updated_floor_count[floor];
		dll_traverse(node, floors[floor])
		{
			p = (Person*)jval_v(dll_val(node));
			if(p->to - 1 == floor)
				count++;
		}
		floor_diff = e->onfloor - floor;
		if(floor_diff < 0)
			floor_diff *= -1;
		floor_diff++;
		tempCount = count;
		count /= floor_diff;
		if(tempCount)
			count++;
		if(count > nextCount)
		{
			pthread_mutex_unlock(floor_locks + next);
			next = floor;
			nextCount = count;
		}
		else if(floor)
		{
			pthread_mutex_unlock(floor_locks + floor);
		}
	}
	updated_floor_count[next] = 0;
	pthread_mutex_unlock(floor_locks + next);

	return next;
}

void move_elevator_to_floor(Elevator *e)
{
	int next_floor = find_next_floor(e) + 1;
		try_close_door(e);
		move_to_floor(e, next_floor);
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


void load_from_floor(Elevator *e, int dir)
{
	int floor = e->onfloor - 1;
	Dllist node, list;
	Person *p;
	pthread_mutex_lock(e->lock);
	pthread_mutex_lock(floor_locks + floor);
	pthread_mutex_unlock(e->lock);
	list = floors[floor];
	dll_traverse(node, list)
	{
		p = (Person*)jval_v(dll_val(node));
		pthread_mutex_lock(p->lock);
		p->e = e;
		try_open_door(e);
		printf("Signal %s\n", p->lname);
		pthread_cond_signal(p->cond);
		pthread_mutex_lock(e->lock);
		pthread_mutex_unlock(p->lock);
		printf("Wait on signal\n");
		pthread_cond_wait(e->cond, e->lock);
		pthread_mutex_unlock(e->lock);
		node = dll_prev(node);
		dll_delete_node(dll_next(node));
	}
	pthread_mutex_unlock(floor_locks + floor);
}

void *elevator(void *arg)
{
	Elevator *e = (Elevator*)arg;
	int direction = 1;
	int floor = e->onfloor;

	while(1)
	{
		move_elevator_to_floor(e);
		floor = e->onfloor;
		unload_to_floor(e);
		load_from_floor(e, direction);
	}
  return NULL;
}
