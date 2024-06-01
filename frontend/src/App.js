import React from "react";
import { Admin, Resource } from "react-admin";
import { OrderList } from "./page/orderList";
import fakeDataProvider from 'ra-data-fakerest';

const dataProvider = fakeDataProvider({})

export const App = () => (
    <Admin dataProvider={dataProvider}>
      <Resource name="orders" list={OrderList} />
    </Admin>
)

export default App;
