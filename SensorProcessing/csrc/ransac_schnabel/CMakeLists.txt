cmake_minimum_required(VERSION 2.8)
project(pc_ransac)
if(APPLE)
	 include(CMakeForceCompiler)
	 CMAKE_FORCE_C_COMPILER(gcc GNU)
	 CMAKE_FORCE_CXX_COMPILER(g++ GNU)
endif(APPLE)

include_directories(src/)
include_directories(src/MiscLib/)
include_directories(/usr/include/malloc)
FILE(GLOB SRC "src/*.cpp")
FILE(GLOB MISC_SRC "src/MiscLib/*.cpp")

add_library(libransac STATIC  ${SRC} ${MISC_SRC})
add_executable(main main.cpp)
target_link_libraries(main libransac)
