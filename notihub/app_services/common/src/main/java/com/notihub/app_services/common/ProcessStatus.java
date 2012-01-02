/*
Copyright 2011 codeoedoc

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.notihub.app_services.common;

public class ProcessStatus {

	private Integer errorCode;
	private String errorDesp;
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorDesp(String errorDesp) {
		this.errorDesp = errorDesp;
	}
	public String getErrorDesp() {
		return errorDesp;
	}
}
