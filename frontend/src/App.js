import React from "react";
import { Admin, Resource } from "react-admin";
import { OrderList } from "./page/orderList";
import fakeDataProvider from 'ra-data-fakerest';

const dataProvider = fakeDataProvider({
    "orders": [
        //{ "cust_no": 1, "order_no": 2, "goods_code": 3, "net_amt": 4 },
    ]
})

export const App = () => (
    <Admin dataProvider={dataProvider}>
      <Resource name="orders" list={OrderList} />
    </Admin>
)

export default App;
