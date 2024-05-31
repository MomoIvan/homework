import React, {useState} from "react";
import {List, Datagrid, TextField, TextInput} from "react-admin";
import {Button} from "@mui/material";

export const OrderList = () => {
    const [custNo, setCustNo] = useState('');
    const [orderNo, setOrderNo] = useState('');
    const [goodsCode, setGoodsCode] = useState('');

    const handleSearch = () => {
        console.log('CustNo:', custNo);
        console.log('OrderNo:', orderNo);
        console.log('GoodsCode:', goodsCode);
    };

    const OrderFilters = [
        <TextInput key="order_filter_cust_no" source="cust_no" label="CustNo" alwaysOn onChange={e => setCustNo(e.target.value)} />,
        <TextInput key="order_filter_order_no" source="order_no" label="OrderNo" alwaysOn onChange={e => setOrderNo(e.target.value)} />,
        <TextInput key="order_filter_goods_code" source="goods_code" label="GoodsCode" alwaysOn onChange={e => setGoodsCode(e.target.value)} />,
        <Button
            key="order_filter_button"
            variant="contained"
            color="primary"
            sx={{ width: 100, height: 50, alignItems: 'center', justifyContent: 'center'}}
            onClick={handleSearch}
        >
            搜尋
        </Button>
    ];

    return (
        <List
            title="Order List"
            filters={OrderFilters}
            filterDefaultValues={{is_published: true}}
        >
            <Datagrid>
                <TextField key="list_cust_no" source="cust_no" label="CustNo"/>
                <TextField key="list_order_no" source="order_no" label="OrderNo"/>
                <TextField key="list_goods_no" source="goods_code" label="GoodsCode"/>
                <TextField key="list_net_amt" source="net_amt" label="NetAmt"/>
            </Datagrid>
        </List>
    )
};

export default OrderList;