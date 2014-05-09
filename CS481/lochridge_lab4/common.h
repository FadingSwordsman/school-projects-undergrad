#ifndef __read_arr_h
#define __read_arr_h

#define ARR_SIZE 1024
#define STRIDE 8
#define NUM_RUNS 10000000

#define communicate_array(f, fd, arr)	f(fd, arr, ARR_SIZE)


#define read_array(arr, temp, counter) for(counter = 0; counter < ARR_SIZE; counter += STRIDE/8) \
	temp = [counter]

#define get_as_ms(time) ((time.tv_sec*1000) + (time.tv_usec/1000))

#define get_time_diff(start, end) (get_as_ms(end) - get_as_ms(start))

#endif
