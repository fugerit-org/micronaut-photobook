import React, { Component, Fragment } from 'react';
import appService from '../common/app-service';

class Version extends Component {

	constructor(props) {
		super(props);
		this.state = {
			versionInfo: null,
			systemInfo: null
		}
	}

	componentDidMount() {
		var reactState = this;
		appService.doAjaxJson('GET', '/meta/version', null).then(response => {
			if (response.success) {
				reactState.setState({
					versionInfo: response.result
				})
			} else {
				reactState.setState({
					supportedExtensions: null
				})
			}
		})
		appService.doAjaxJson('GET', '/meta/info', null).then(response => {
			if (response.success) {
				reactState.setState({
					systemInfo: response.result
				})
			} else {
				reactState.setState({
					supportedExtensions: null
				})
			}
		})
	}

	render() {
		let printVersion = 'loading...';
		if ( this.state.versionInfo != null ) {
			printVersion = this.state.versionInfo.version + ' ('+this.state.versionInfo.revision+')';
		}
		if ( this.state.systemInfo != null ) {
			printVersion =  printVersion + ' ['+ this.state.systemInfo + ']'
		}
		return <Fragment>
			<p>Versione : {printVersion}</p>
		</Fragment>
	}

}

export default Version;