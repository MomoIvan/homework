import React from "react";
import { Admin, Resource } from "react-admin";
import { OrderList } from "./page/orderList";
import { PostList } from "./page/uploadCSV";
import fakeDataProvider from 'ra-data-fakerest';

const dataProvider = fakeDataProvider({
});

export const App = () => (
    <Admin dataProvider={dataProvider}>
      <Resource name="orders" list={OrderList} />
      <Resource name="upload" list={PostList} />
    </Admin>
)

export default App;
