package com.example.irek.homecontrolbetaversion.ui.home;

import com.example.irek.homecontrolbetaversion.data.model.DataToSend;
import com.example.irek.homecontrolbetaversion.ui.base.BaseView;

/**
 * Created by Wojtek on 23.04.2017.
 */

public interface HomeView extends BaseView {
    public void refreshUI(String tempVal);
    public void initViewFields(DataToSend dts);
}
