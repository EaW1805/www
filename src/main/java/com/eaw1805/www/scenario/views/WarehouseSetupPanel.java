package com.eaw1805.www.scenario.views;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.data.dto.web.economy.GoodDTO;
import com.eaw1805.data.dto.web.economy.StoredGoodDTO;
import com.eaw1805.data.dto.web.economy.WarehouseDTO;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.fieldbattle.widgets.shared.TextBoxEditable;


public class WarehouseSetupPanel extends VerticalPanel implements GoodConstants {

    public WarehouseSetupPanel() {
        getElement().getStyle().setBackgroundColor("grey");



//        add(grid);
    }
    public void initPanel (final RegionDTO region) {
        clear();
        final Grid grid = new Grid(18, GOOD_LAST + 1);

        int row = 0;
        int column = 0;
        for (final GoodDTO good : EditorStore.getInstance().getGoods()) {
            column++;
            Image goodImg = new Image("http://static.eaw1805.com/images/goods/good-" + good.getGoodId() + ".png");
            goodImg.setSize("14px", "14px");
            goodImg.setTitle(good.getName());
            grid.setWidget(row, column, goodImg);
        }

        for (final NationDTO nation : EditorStore.getInstance().getNations()) {
            if (nation.getNationId() == -1) {
                continue;
            }
            column = 0;
            row++;
            final Image nationImg = new Image("http://static.eaw1805.com/images/nations/nation-" + nation.getNationId() + "-120.png");
            nationImg.setTitle(nation.getName());
            nationImg.setHeight("14px");
            grid.setWidget(row, column, nationImg);
            final WarehouseDTO currentWarehouse = EditorStore.getInstance().getWarehouseByRegionNation(region.getRegionId(), nation.getNationId());

            for (final StoredGoodDTO good : currentWarehouse.getGoodsDTO().values()) {
                column++;
                final TextBoxEditable box = new TextBoxEditable("");
                box.getElement().getStyle().setFontSize(7d, Style.Unit.PT);
                box.setWidth("50px");
                box.setText(String.valueOf(good.getQte()));
                box.initHandler(new BasicHandler() {
                    @Override
                    public void run() {
                        good.setQte(Integer.parseInt(box.getText()));
                    }
                });
                grid.setWidget(row, column, box);
            }

        }
        add(grid);
    }
}
