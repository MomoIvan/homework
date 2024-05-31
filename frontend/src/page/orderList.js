import React  from "react";

import { List, Datagrid, TextField } from "react-admin";

export const OrderList = () => (
    <List perPage={50}>
        <Datagrid>
            <TextField source="cust_no" label="CustNo" />
            <TextField source="order_no" label="OrderNo" />
            <TextField source="goods_code" label="GoodsCode" />
            <TextField source="net_amt" label="NetAmt" />
        </Datagrid>
    </List>
);