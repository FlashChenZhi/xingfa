// import './style/myantd.less';
import ReactDOM from 'react-dom';
import React from 'react';
import {Router, Route, hashHistory, IndexRedirect} from 'react-router';
import LoginMain from '../components/login/LoginMain';
import LoginForm from '../components/login/LoginForm';
import IndexMain from '../components/layout/IndexMain';
import NewInputArea from '../components/InOrOut/NewInputArea';
import InputAreaAgain from '../components/InOrOut/InputAreaAgain';
import OutputArea from '../components/InOrOut/OutputArea';
import InventoryQuery from '../components/query/InventoryQuery';
import InputPerformanceQuery from '../components/query/InputPerformanceQuery';
import OutputPerformanceQuery from '../components/query/OutputPerformanceQuery';
import OnlineTaskQuery from '../components/query/OnlineTaskQuery';
import ReserveQuery from '../components/query/ReserveQuery';
import LocationQuery from '../components/query/LocationQuery';
import BlockQuery from '../components/query/BlockQuery';
import MessageQuery from '../components/query/MessageQuery';
import InventoryOutQuery from '../components/InOrOut/InventoryOutQuery';
import AsrsJobQuery from '../components/query/AsrsJobQuery';
import RecvPlanQuery from '../components/query/RecvPlanQuery';
import CreateReceivingPlan from '../components/InOrOut/CreateReceivingPlan';
import SkuQuery from '../components/query/SkuQuery';
import ModifyLocation from '../components/query/ModifyLocation';
import MessageLogQuery from '../components/query/MessageLogQuery';
import SendMessage from '../components/query/SendMessage';
import PutInStorage from '../components/InOrOut/PutInStorage';
import OrderInquiry from '../components/InOrOut/OrderInquiry';
import PlatformSwitch from '../components/InOrOut/PlatformSwitch';
import FindInventory from '../components/InOrOut/FindInventory';
import FindOutOrInWarehouse from '../components/InOrOut/FindOutOrInWarehouse';
import AssignsTheStorehouse from '../components/InOrOut/AssignsTheStorehouse';
class Index extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Router history={hashHistory}>
                <Route path="/home" component={IndexMain}>
                    <Route path="/NewInputArea" component={NewInputArea}/>
                    <Route path="/InputAreaAgain" component={InputAreaAgain}/>
                    <Route path="/OutputArea" component={OutputArea}/>
                    <Route path="/OnlineTaskQuery" component={OnlineTaskQuery}/>
                    <Route path="/InputPerformanceQuery" component={InputPerformanceQuery}/>
                    <Route path="/OutputPerformanceQuery" component={OutputPerformanceQuery}/>
                    <Route path="/InventoryQuery" component={InventoryQuery}/>
                    <Route path="/ReserveQuery" component={ReserveQuery}/>
                    <Route path="/LocationQuery" component={LocationQuery}/>
                    <Route path="/BlockQuery" component={BlockQuery}/>
                    <Route path="/MessageQuery" component={MessageQuery}/>
                    <Route path="/InventoryOutQuery" component={InventoryOutQuery}/>
                    <Route path="/AsrsJobQuery" component={AsrsJobQuery}/>
                    <Route path="/RecvPlanQuery" component={RecvPlanQuery}/>
                    <Route path="/CreateReceivingPlan" components={CreateReceivingPlan}/>
                    <Route path="/SkuQuery" components={SkuQuery}/>
                    <Route path="/ModifyLocation" components={ModifyLocation}/>
                    <Route path="/MessageLogQuery" components={MessageLogQuery}/>
                    <Route path="/SendMessage" components={SendMessage}/>
                    <Route path="/PutInStorage" components={PutInStorage}/>
                    <Route path="/OrderInquiry" components={OrderInquiry}/>
                    <Route path="/PlatformSwitch" components={PlatformSwitch}/>
                    <Route path="/FindInventory" components={FindInventory}/>
                    <Route path="/FindOutOrInWarehouse" components={FindOutOrInWarehouse}/>
                    <Route path="/AssignsTheStorehouse" components={AssignsTheStorehouse}/>
                </Route>
                <Route path="/" component={LoginMain}>
                    <IndexRedirect to="/login"/>
                    <Route path="/login" component={LoginForm}/>
                </Route>
            </Router>
        );
    }
}
;

ReactDOM.render(<Index />, document.getElementById('root'));
