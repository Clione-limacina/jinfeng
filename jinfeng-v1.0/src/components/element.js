// 按需引入 Element 组件
import { Button, Dropdown, DropdownMenu, DropdownItem, Menu, MenuItem, Breadcrumb, BreadcrumbItem, FormItem, Input, Select, Option, Col, DatePicker, TableColumn, MessageBox, Message, Form, Table, Pagination, Upload, Dialog } from 'element-ui'

let elementUIs = [Button, Dropdown, DropdownMenu, DropdownItem, Menu, MenuItem, Breadcrumb, BreadcrumbItem, FormItem, Input, Select, Option, Col, DatePicker, TableColumn, Form, Table, Pagination, Upload, Dialog]

export default {
    install(Vue) {
        elementUIs.forEach(elementUI => [
            // Vue.component(elementUI.name, elementUI)
            Vue.use(elementUI)    // 也可以使用 Vue.use()
        ])

        //引入弹窗组件和提示框组件
        Vue.prototype.$message = Message;
        Vue.prototype.$confirm = MessageBox.confirm;
    }
}