package com.github.davidmoten.rx.jdbc;

import rx.Observable;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.functions.Func1;

import com.github.davidmoten.rx.OperatorFromOperation;

/**
 * {@link Operator} corresonding to {@link QueryUpdateOperation}.
 */
public class QueryUpdateOperator<R> implements Operator<Integer, R> {

    private final OperatorFromOperation<Integer, R> operator;

    /**
     * Constructor.
     * 
     * @param builder
     * @param operatorType
     */
    QueryUpdateOperator(final QueryUpdate.Builder builder,
            final OperatorType operatorType) {
        operator = new OperatorFromOperation<Integer, R>(
                new Func1<Observable<R>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Observable<R> observable) {
                        if (operatorType == OperatorType.PARAMETER)
                            return builder.parameters(observable).count();
                        else if (operatorType == OperatorType.DEPENDENCY)
                            // dependency
                            return builder.dependsOn(observable).count();
                        else throw new RuntimeException("does not handle " + operatorType);
                    }
                });
    }

    @Override
    public Subscriber<? super R> call(Subscriber<? super Integer> subscriber) {
        return operator.call(subscriber);
    }
}
