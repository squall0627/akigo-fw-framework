package com.akigo.test.mocker;

import java.util.List;

import com.akigo.test.matcher.SimpleMatcher;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;

/**
 * 機能名 : メソッドパラメータースタブインタフェース<br>
 * <br>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/2/25
 */
public interface PrivatelyExpectedArguments {
    /**
     * パラメーター渡す処理<br>
     * パラメーターに既にMatcherを付与する場合は、そのまま利用、<br>
     * そうでない場合、{@link SimpleMatcher#match(Object)}でMatcherを生成して付与する。<br>
     * <br>
     *
     * @param firstArgument       1個目パラメーター
     * @param additionalArguments 2個目以降パラメーター
     */
    void withArguments(Object firstArgument, Object... additionalArguments);

    /**
     * パラメーターなしメソッドのスタブ<br>
     */
    void withNoArguments();

    /**
     * パラメーターにMatcherを付与するかどうかの判断処理<br>
     * <br>
     *
     * @return true of false
     */
    default boolean isPresentMatcher() {
        MockingProgress mockingProgress = new ThreadSafeMockingProgress();
        ArgumentMatcherStorage argumentMatcherStorage = mockingProgress.getArgumentMatcherStorage();
        List<LocalizedMatcher> lastMatchers = argumentMatcherStorage.pullLocalizedMatchers();
        if (!lastMatchers.isEmpty()) {
            // PollしたMatcherをArgumentMatcherStorageに再Push
            for (LocalizedMatcher localizedMatcher : lastMatchers) {
                argumentMatcherStorage.reportMatcher(localizedMatcher.getActualMatcher());
            }
            return false;
        }
        return true;
    }

}
