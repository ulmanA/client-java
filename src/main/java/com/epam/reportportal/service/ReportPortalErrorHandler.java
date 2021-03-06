/*
 * Copyright 2016 EPAM Systems
 *
 *
 * This file is part of EPAM Report Portal.
 * https://github.com/reportportal/client
 *
 * Report Portal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Report Portal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Report Portal.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epam.reportportal.service;

import com.epam.reportportal.exception.GeneralReportPortalException;
import com.epam.reportportal.exception.ReportPortalException;
import com.epam.reportportal.restendpoint.http.DefaultErrorHandler;
import com.epam.reportportal.restendpoint.http.HttpMethod;
import com.epam.reportportal.restendpoint.http.exception.RestEndpointIOException;
import com.epam.reportportal.restendpoint.serializer.Serializer;
import com.epam.ta.reportportal.ws.model.ErrorRS;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import java.io.IOException;
import java.net.URI;

/**
 * Report Portal Error Handler<br>
 * Converts error from Endpoint to ReportPortal-related errors
 *
 * @author Andrei Varabyeu
 */
public class ReportPortalErrorHandler extends DefaultErrorHandler {

	private Serializer serializer;

	public ReportPortalErrorHandler(Serializer serializer) {
		this.serializer = serializer;
	}

	@Override
	protected void handleError(URI requestUri, HttpMethod requestMethod, int statusCode, String statusMessage, ByteSource errorBody)
			throws RestEndpointIOException {
		try {
			//read the body
			final byte[] body = errorBody.read();

			//try to deserialize an error
			ErrorRS errorRS = deserializeError(body);
			if (null != errorRS) {

				//ok, it's known ReportPortal error
				throw new ReportPortalException(statusCode, statusMessage, errorRS);
			} else {
				//there is some unknown error since we cannot de-serialize it into default error object
				throw new GeneralReportPortalException(statusCode, statusMessage, new String(body, Charsets.UTF_8));
			}

		} catch (IOException e) {
			//cannot read the body. just throw the general error
			throw new GeneralReportPortalException(statusCode, statusMessage, "Cannot read the response");
		}

	}

	/**
	 * Try to deserialize an error body
	 *
	 * @param content content to be deserialized
	 * @return Serialized object or NULL if it's impossible
	 */
	private ErrorRS deserializeError(byte[] content) {
		try {
			if (null != content) {
				return serializer.deserialize(content, ErrorRS.class);
			} else {
				return null;
			}

		} catch (Exception e) {
			return null;
		}

	}
}
