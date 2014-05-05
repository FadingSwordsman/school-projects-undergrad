#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "common.h"

int *pipe_ids;
int *buffer;

void read_process()
{
	int temp, counter;
	communicate_array(read, pipe_ids[0], buffer);
	for(counter = 0; counter < ARR_SIZE; counter += STRIDE/sizeof(int))
	{
		temp = buffer[counter];
		printf("%d\n", temp);
	}
}

void write_process()
{
	int counter;
	for(counter = 0; counter < ARR_SIZE; counter++)
		buffer[counter]++;
	communicate_array(write, pipe_ids[1], buffer);
}

void parent()
{
	int count;
	for(count = 0; count < ARR_SIZE; count++)
		buffer[count] = count;
	for(count = 0; count < NUM_RUNS; count++)
	{
		write_process();
		read_process();
	}
}

void child()
{
	int count;
	for(count = 0; count < NUM_RUNS; count++)
	{
		read_process();
		write_process();
	}
}

int main(int argc, char **argv)
{
	buffer = (int*) malloc(ARR_SIZE*sizeof(int));
	pipe_ids = (int*)malloc(2*sizeof(int));
	pipe(pipe_ids);
	if(fork())
		parent();
	else
		child();
	free(pipe_ids);
	return 0;
}
