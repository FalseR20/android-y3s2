package com.falser.lab1;

public class Constants {
    final static int N_ROWS = 5;
    final static int N_COLUMNS = 4;
    final static int N_IMAGES = N_ROWS * N_COLUMNS;
    final static int N_STATES = 2;
    final static int N_SETS = N_IMAGES / N_STATES;
    final static int N_TRIES = (int) (N_IMAGES * N_STATES * 0.75);
}
