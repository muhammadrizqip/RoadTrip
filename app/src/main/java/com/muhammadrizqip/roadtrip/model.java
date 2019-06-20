package com.muhammadrizqip.roadtrip;

public class model {
    private String id, wisata;
    private int biaya;

    public model() {
    }

    public model(String id, String wisata, int biaya) {
        this.id = id;
        this.wisata = wisata;
        this.biaya = biaya;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getWisata() {
        return wisata;
    }

    public void setWisata(String wisata) {
        this.wisata = wisata;
    }

    public int getBiaya() {
        return biaya;
    }

    public void setBiaya(int biaya) {
        this.biaya = biaya;
    }


}
