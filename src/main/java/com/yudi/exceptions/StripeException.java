package com.yudi.exceptions;

import com.stripe.exception.CardException; /**
 * Created by Yudi on 02/02/2018.
 */
public class StripeException extends RuntimeException {
    public StripeException(Throwable e) {
        super(e);
    }
}
