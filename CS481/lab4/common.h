#ifndef __read_arr_h
#define __read_arr_h

#define ARR_SIZE 1024
#define STRIDE 8
#define NUM_RUNS 1000000

#define communicate_array(f, fd, arr)	f(fd, arr, ARR_SIZE)


#define read_array(arr, temp, counter) for(counter = 0; counter < ARR_SIZE; counter += STRIDE/8) \
	temp = [counter]

#endif
