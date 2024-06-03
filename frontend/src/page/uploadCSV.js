import React, {useState} from "react";
import {
    FileInput,
    FileField,
    SimpleForm,
    useNotify,
    ListContextProvider,
    Title,
    useList,
    Datagrid,
    TextField, Pagination
} from "react-admin";
import ApiDataProvider from "../provider/apiDataProvider";
import {Card} from "@mui/material";

const UploadSVCToolbar = ({ uploadSuccess }) => {
    const [file, setSelectedFile] = useState(null);
    const [key, setKey] = useState(0);
    const notify = useNotify();

    const handleSave = async() => {
        try {
            const response = await ApiDataProvider.upload('orders/import', file)

            const respData = response.data

            var message = respData.errorCode === 0 ? 'Upload Data Successfully!!!!' : `Upload Data Failed!!!! ${response.data.errorMessage}`;
            notify( message, { type: 'info'});

            if (respData.errorCode === 0 ) {
                uploadSuccess(respData.data)
            }
        } catch (error) {
            notify(`Error uploading file: ${error.message}`, { type: 'warning' });
        }

        setKey(key => key + 1)
    };

    return (
        <SimpleForm onSubmit={handleSave} key={key}>
            <FileInput source="" accept="text/csv" name="upload_file_input" onChange={setSelectedFile}>
                <FileField source="src" title="title"/>
            </FileInput>
        </SimpleForm>
    );
};

export const PostList = () => {
    const [listContext, setListContext] = useState([]);
    const [pageLimit, setPageLimit] = useState(50);
    const OrderListPagination = () => <Pagination rowsPerPageOptions={[50, 100, 200]}/>;

    const handleUploadSuccess = (orders) => {
        if (orders.length === 0) {
            return
        }

        setListContext(orders)
    }

    return (
        <ListContextProvider
            value={useList({
                data: listContext,
                total: listContext.length,
                perPage: pageLimit,
                setPerPage: setPageLimit,
            })}
        >
            <div>
                <Title title="Upload CSC"/>
                <UploadSVCToolbar uploadSuccess={handleUploadSuccess}/>
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
    )
};