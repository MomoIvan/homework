import React from "react";
import { Admin, Resource } from "react-admin";
import { OrderList } from "./page/orderList";
import jsonServerProvider from 'ra-data-json-server';

const dataProvider = jsonServerProvider('http://localhost:8080/api/v1');

export const App = () => (
    <Admin dataProvider={dataProvider}>
      <Resource name="orders" list={OrderList} />
    </Admin>
)

export default App;
