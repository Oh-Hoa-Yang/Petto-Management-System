package com.example.pettomanagementsystem;

public interface ContactListInterface {
    void onContactClick(int position);
    void onLongContactClick(int position);
    int getCallButtonVisibility();
    int getEditButtonVisibility();
    int getAddButtonVisibility();
    int getDeleteButtonVisibility();
}
