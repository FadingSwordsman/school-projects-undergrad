#ifndef __test_common_h
#define __test_common_h

#define read_file(func, buffer, stride, counter, filesize, file) \
	for(counter = 0; counter + stride < filesize; counter += stride) \
		func(file, buffer + counter, stride); \
	if(counter < filesize)\
		func(file, buffer + counter, filesize - counter);

#endif
