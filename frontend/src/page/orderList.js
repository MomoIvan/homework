import React, {useState} from "react";
import {
    Title,
    Datagrid,
    TextField,
    ListContextProvider,
    useList,
    TextInput,
    Form,
    useNotify,
    Pagination,
} from "react-admin";
import {Button, Card} from "@mui/material";
import ApiDataProvider from "../provider/apiDataProvider";

const OrderSearchToolbar = ({ onSearch }) => {
    const inputStyle = {
        height: '45px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        flexWrap: 'wrap'
    }

    return (
        <Form
            onSubmit={data => {
                onSearch(data);
            }}
        >
            <div style={{display: 'flex', gap: '20px', alignItems: 'center', justifyContent: 'center', padding: '5px'}}>
                <TextInput name="cust_no" label="Cust No" source="cust_no"  fullWidth style={inputStyle} />
                <TextInput name="order_no" label="Order No" source="order_no" fullWidth style={inputStyle}/>
                <TextInput name="goods_code" label="Goods Code" source="goods_code" fullWidth style={inputStyle} />
                <Button type="submit" variant="contained" color="primary" style={inputStyle} >
                    搜尋
                </Button>
            </div>
        </Form>
    );
};

export const OrderList = () => {
    const [listContext, setListContext] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [pageLimit, setPageLimit] = useState(50);
    const [totalElements, setTotalElements] = useState(0);
    const OrderListPagination = () => <Pagination rowsPerPageOptions={[50, 100, 200]}/>;
    const notify = useNotify();

    const handleSearch = async (filters) => {
        try {
            const response = await ApiDataProvider.getList('orders/search', {
                filter: filters,
            });

            setTotalElements(response.data.totalElements)
            setCurrentPage(response.data.currentPage + 1)

            setListContext(response.data.content);
        } catch (error) {
            notify(`Error fetching data: ${error.message}`, { type: 'warning'})
        }
    };

    return (
        <ListContextProvider
            value={useList({
                data: listContext || [],
                total: totalElements,
                page: currentPage,
                perPage: pageLimit,
                setPage: setCurrentPage,
                setPerPage: setPageLimit,
            })}
        >
            <div>
                <Title title="Order List" />
                <OrderSearchToolbar onSearch={handleSearch} />
                <Card>
                    <Datagrid>
                        <TextField source="cust_no" label="Cust No" />
                        <TextField source="order_date" label="Order Date" />
                        <TextField source="order_no" label="Order No" />
                        <TextField source="goods_code" label="Goods Code" />
                        <TextField source="net_amt" label="Net Amt" />
                        <TextField source="feedback_percent" label="Feedback Percent" />
                        <TextField source="feedback_money" label="Feedback Money" />
                    </Datagrid>
                </Card>
            </div>
            <OrderListPagination />
        </ListContextProvider>
    );
};

export default OrderList;