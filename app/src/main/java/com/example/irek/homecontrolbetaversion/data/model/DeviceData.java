package com.example.irek.homecontrolbetaversion.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Wojtek on 20.04.2017.
 */

@Entity
public class DeviceData {
    @Id(autoincrement = true)
    private Long id;

    // dom
    private Long tempDom;

    // ogrod
    private Long wilgotnosc;

    // pokoj dziecka
    private Long tempPokojDzieci;

    private boolean wykrytoRuch;

    @Generated(hash = 574969081)
    public DeviceData(Long id, Long tempDom, Long wilgotnosc, Long tempPokojDzieci,
            boolean wykrytoRuch) {
        this.id = id;
        this.tempDom = tempDom;
        this.wilgotnosc = wilgotnosc;
        this.tempPokojDzieci = tempPokojDzieci;
        this.wykrytoRuch = wykrytoRuch;
    }

    @Generated(hash = 929507321)
    public DeviceData() {
    }

    public DeviceData(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWilgotnosc() {
        return wilgotnosc;
    }

    public void setWilgotnosc(Long wilgotnosc) {
        this.wilgotnosc = wilgotnosc;
    }

    public Long getTempPokojDzieci() {
        return tempPokojDzieci;
    }

    public void setTempPokojDzieci(Long tempPokojDzieci) {
        this.tempPokojDzieci = tempPokojDzieci;
    }

    public Long getTempDom() {
        return this.tempDom;
    }

    public void setTempDom(Long tempDom) {
        this.tempDom = tempDom;
    }

    public boolean isWykrytoRuch() {
        return wykrytoRuch;
    }

    public void setWykrytoRuch(boolean wykrytoRuch) {
        this.wykrytoRuch = wykrytoRuch;
    }

    public boolean getWykrytoRuch() {
        return this.wykrytoRuch;
    }
}
