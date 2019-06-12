package com.akigo.test.mocker;

import java.util.Arrays;

import com.akigo.test.matcher.SimpleMatcher;
import org.mockito.Matchers;

/**
 * 機能名 : メソッドパラメータースタブインタフェースImpl<br>
 * <br>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/2/25
 */
public class DefaultPrivatelyExpectedArguments implements PrivatelyExpectedArguments,
        SimpleMatcher {

    private org.powermock.api.mockito.expectation.PrivatelyExpectedArguments argumentsStubber;

    /**
     * コンストラクター<br>
     *
     * @param argumentsStubber {@link org.powermock.api.mockito.expectation.PrivatelyExpectedArguments}
     */
    protected DefaultPrivatelyExpectedArguments(
            org.powermock.api.mockito.expectation.PrivatelyExpectedArguments argumentsStubber) {
        this.argumentsStubber = argumentsStubber;
    }

    @Override
    public void withArguments(Object firstArgument, Object... additionalArguments) {
        if (isPresentMatcher()) {
            Object firstArgumentMatcher = Matchers.argThat(match(firstArgument));
            Object[] additionalArgumentsMatcher = Arrays.asList(additionalArguments)
                    .stream()
                    .map(a -> Matchers.argThat(match(a))).toArray();
            try {
                this.argumentsStubber.withArguments(
                        firstArgumentMatcher,
                        additionalArgumentsMatcher);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                this.argumentsStubber.withArguments(
                        firstArgument,
                        additionalArguments);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void withNoArguments() {
        try {
            this.argumentsStubber.withNoArguments();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
