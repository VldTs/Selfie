package ru.tsarcom.slff.slff;

/**
 * Created by USRSLM on 26.11.14.
 */
public class MineCompare {

    int id_compare;
    String date_crt;
    String path_left;
    String path_right;
    int price;
    int image;
    boolean box;


    MineCompare(int _id_compare, String _date_crt, String _path_left, String _path_right) {
        id_compare = _id_compare;
        date_crt = _date_crt;
        path_left = _path_left;
        path_right = _path_right;
    }
}
