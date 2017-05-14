package com.example.irek.homecontrolbetaversion.data.model;

/**
 * Created by Wojtek on 10.05.2017.
 */

public class DataToSend {
    // dom
    public Long prefTempDom;
    public boolean czujnikRuchu;
    public boolean zasilanieAnten;
    public boolean oswietlenieDom;

    // ogrod
    public Long uruchomienieZraszaczy;
    public boolean blokadaBramy;
    public boolean fontanna;
    public boolean oswietlenieOgrodu;

    // dzieci
    public Long prefTempDzieci;
    public Long jasnoscOswietlenia;
    public boolean internetSwitch;
    public boolean tvsatSwitch;

    public DataToSend(Long prefTempDom, boolean czujnikRuchu, boolean zasilanieAnten, boolean oswietlenieDom, Long uruchomienieZraszaczy, boolean blokadaBramy, boolean fontanna, boolean oswietlenieOgrodu, Long prefTempDzieci, Long jasnoscOswietlenia, boolean internetSwitch, boolean tvsatSwitch) {
        this.prefTempDom = prefTempDom;
        this.czujnikRuchu = czujnikRuchu;
        this.zasilanieAnten = zasilanieAnten;
        this.oswietlenieDom = oswietlenieDom;
        this.uruchomienieZraszaczy = uruchomienieZraszaczy;
        this.blokadaBramy = blokadaBramy;
        this.fontanna = fontanna;
        this.oswietlenieOgrodu = oswietlenieOgrodu;
        this.prefTempDzieci = prefTempDzieci;
        this.jasnoscOswietlenia = jasnoscOswietlenia;
        this.internetSwitch = internetSwitch;
        this.tvsatSwitch = tvsatSwitch;
    }

    public DataToSend() {
    }

    public Long getPrefTempDom() {
        return prefTempDom;
    }

    public void setPrefTempDom(Long prefTempDom) {
        this.prefTempDom = prefTempDom;
    }

    public boolean isCzujnikRuchu() {
        return czujnikRuchu;
    }

    public void setCzujnikRuchu(boolean czujnikRuchu) {
        this.czujnikRuchu = czujnikRuchu;
    }

    public boolean isZasilanieAnten() {
        return zasilanieAnten;
    }

    public void setZasilanieAnten(boolean zasilanieAnten) {
        this.zasilanieAnten = zasilanieAnten;
    }

    public boolean isOswietlenieDom() {
        return oswietlenieDom;
    }

    public void setOswietlenieDom(boolean oswietlenieDom) {
        this.oswietlenieDom = oswietlenieDom;
    }

    public Long getUruchomienieZraszaczy() {
        return uruchomienieZraszaczy;
    }

    public void setUruchomienieZraszaczy(Long uruchomienieZraszaczy) {
        this.uruchomienieZraszaczy = uruchomienieZraszaczy;
    }

    public boolean isBlokadaBramy() {
        return blokadaBramy;
    }

    public void setBlokadaBramy(boolean blokadaBramy) {
        this.blokadaBramy = blokadaBramy;
    }

    public boolean isFontanna() {
        return fontanna;
    }

    public void setFontanna(boolean fontanna) {
        this.fontanna = fontanna;
    }

    public boolean isOswietlenieOgrodu() {
        return oswietlenieOgrodu;
    }

    public void setOswietlenieOgrodu(boolean oswietlenieOgrodu) {
        this.oswietlenieOgrodu = oswietlenieOgrodu;
    }

    public Long getPrefTempDzieci() {
        return prefTempDzieci;
    }

    public void setPrefTempDzieci(Long prefTempDzieci) {
        this.prefTempDzieci = prefTempDzieci;
    }

    public Long getJasnoscOswietlenia() {
        return jasnoscOswietlenia;
    }

    public void setJasnoscOswietlenia(Long jasnoscOswietlenia) {
        this.jasnoscOswietlenia = jasnoscOswietlenia;
    }

    public boolean isInternetSwitch() {
        return internetSwitch;
    }

    public void setInternetSwitch(boolean internetSwitch) {
        this.internetSwitch = internetSwitch;
    }

    public boolean isTvsatSwitch() {
        return tvsatSwitch;
    }

    public void setTvsatSwitch(boolean tvsatSwitch) {
        this.tvsatSwitch = tvsatSwitch;
    }
}
