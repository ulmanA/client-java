package com.epam.reportportal.service;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Launch {
	static final Logger LOGGER = LoggerFactory.getLogger(Launch.class);
	private final ListenerParameters parameters;

	Launch(ListenerParameters parameters) {
		this.parameters = parameters;
	}

	abstract public Maybe<String> start();

	/**
	 * Finishes launch in ReportPortal. Blocks until all items are reported correctly
	 *
	 * @param rq Finish RQ
	 */
	abstract public void finish(final FinishExecutionRQ rq);

	/**
	 * Starts new test item in ReportPortal asynchronously (non-blocking)
	 *
	 * @param rq Start RQ
	 * @return Test Item ID promise
	 */
	abstract public Maybe<String> startTestItem(final StartTestItemRQ rq);

	/**
	 * Starts new test item in ReportPortal asynchronously (non-blocking)
	 *
	 * @param rq Start RQ
	 * @return Test Item ID promise
	 */
	abstract public Maybe<String> startTestItem(final Maybe<String> parentId, final StartTestItemRQ rq);

	/**
	 * Starts new test item in ReportPortal in respect of provided retry
	 *
	 * @param parentId promise of ID of parent
	 * @param retryOf  promise of ID of retried element
	 * @param rq       promise of ID of request
	 * @return Promise of Test Item ID
	 */
	abstract public Maybe<String> startTestItem(final Maybe<String> parentId, final Maybe<String> retryOf, final StartTestItemRQ rq);

	/**
	 * Finishes Test Item in ReportPortal. Non-blocking. Schedules finish after success of all child items
	 *
	 * @param itemId Item ID promise
	 * @param rq     Finish request
	 */
	abstract public void finishTestItem(Maybe<String> itemId, final FinishTestItemRQ rq);

	public ListenerParameters getParameters() {
		return this.parameters;
	}

	/**
	 * Implementation for disabled Reporting
	 */
	public static final Launch NOOP_LAUNCH = new Launch(new ListenerParameters()) {

		@Override
		public Maybe<String> start() {
			return Maybe.empty();
		}

		@Override
		public void finish(FinishExecutionRQ rq) {

		}

		@Override
		public Maybe<String> startTestItem(StartTestItemRQ rq) {
			return Maybe.empty();
		}

		@Override
		public Maybe<String> startTestItem(Maybe<String> parentId, StartTestItemRQ rq) {
			return Maybe.empty();
		}

		@Override
		public Maybe<String> startTestItem(Maybe<String> parentId, Maybe<String> retryOf, StartTestItemRQ rq) {
			return Maybe.empty();
		}

		@Override
		public void finishTestItem(Maybe<String> itemId, FinishTestItemRQ rq) {

		}
	};
}
