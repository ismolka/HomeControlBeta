package com.example.irek.homecontrolbetaversion.ui.children;

import com.example.irek.homecontrolbetaversion.data.model.DataToSend;
import com.example.irek.homecontrolbetaversion.ui.base.BaseView;

/**
 * Created by Wojtek on 24.04.2017.
 */

public interface ChildrenView extends BaseView {
    public void refreshUI(String tempVal);
    public void initViewFields(DataToSend dts);
}
