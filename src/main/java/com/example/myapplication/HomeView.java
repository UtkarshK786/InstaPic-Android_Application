package com.example.myapplication;

public class HomeView {
    private String imageResource;
    private String text;

    public HomeView(String imageResource,String text){
        this.imageResource=imageResource;
        this.text=text;
    }

    public String getImageResource(){
return imageResource;
    }

    public String getText(){
        return text;
    }
}
