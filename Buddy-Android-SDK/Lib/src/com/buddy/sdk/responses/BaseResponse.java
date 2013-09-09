/*
 * Copyright (C) 2012 Buddy Platform, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.buddy.sdk.responses;

public class BaseResponse {
    private Throwable throwable = null;
    private String errorMessage = "";

    public Boolean isCompleted() {
        return throwable == null;
    }

    public void setThrowable(Throwable ex) {
        this.throwable = ex;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public void setErrorMessage(String error) {
        this.errorMessage = error;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
