package com.akigo.test.matcher;

import static org.hamcrest.beans.PropertyUtil.NO_ARGUMENTS;
import static org.hamcrest.beans.PropertyUtil.propertyDescriptorsFor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * 機能名 : プロパティMatcher<br>
 * <br>
 *
 * @param <T>
 * @author 作成者：chenhao
 * @since 作成日：2019/2/25
 */
public class SamePropertyValuesAs<T> extends TypeSafeDiagnosingMatcher<T> implements SimpleMatcher {

    private final T expectedBean;
    private final Set<String> propertyNames;
    private final List<PropertyMatcher> propertyMatchers;

    /**
     * 既存改造<br>
     * Field存在しないPropertyDescriptorを排除 <br>
     *
     * @param fromObj   Object
     * @param stopClass Class<Object>
     * @return PropertyDescriptor[]
     */
    private PropertyDescriptor[] propertyDescriptorsOfHasFieldFor(Object fromObj, Class<
            Object> stopClass) {
        PropertyDescriptor[] descriptors = propertyDescriptorsFor(fromObj, stopClass);
        // Field存在しないPropertyDescriptorを排除
        List<PropertyDescriptor> descriptorList = Arrays.stream(descriptors).filter(des -> {
            try {
                return fromObj.getClass().getDeclaredField(des.getDisplayName()) != null;
            } catch (NoSuchFieldException | SecurityException e) {
                return false;
            }
        }).collect(Collectors.toList());
        PropertyDescriptor[] descriptorsOfHasField = descriptorList.toArray(
                new PropertyDescriptor[descriptorList.size()]);
        return descriptorsOfHasField;
    }

    public SamePropertyValuesAs(T expectedBean) {
//        PropertyDescriptor[] descriptors = propertyDescriptors(expectedBean, Object.class);
        // Field存在しないPropertyDescriptorを排除
        PropertyDescriptor[] descriptors = propertyDescriptorsOfHasFieldFor(
                expectedBean,
                Object.class);
        this.expectedBean = expectedBean;
        this.propertyNames = propertyNamesFrom(descriptors);
        this.propertyMatchers = propertyMatchersFor(expectedBean, descriptors);
    }

    @Override
    public boolean matchesSafely(T bean, Description mismatch) {
        return isCompatibleType(bean, mismatch)
                && hasNoExtraProperties(bean, mismatch)
                && hasMatchingValues(bean, mismatch);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("same property values as " + expectedBean.getClass().getSimpleName())
                .appendList(" [", ", ", "]", propertyMatchers);
    }

    private boolean isCompatibleType(T item, Description mismatchDescription) {
        if (!expectedBean.getClass().isAssignableFrom(item.getClass())) {
            mismatchDescription.appendText(
                    "is incompatible type: " + item.getClass().getSimpleName());
            return false;
        }
        return true;
    }

    private boolean hasNoExtraProperties(T item, Description mismatchDescription) {
//        Set<String> actualPropertyNames = propertyNamesFrom(
//            propertyDescriptorsFor(item, Object.class));
        // Field存在しないPropertyDescriptorを排除
        Set<String> actualPropertyNames = propertyNamesFrom(
                propertyDescriptorsOfHasFieldFor(item, Object.class));
        actualPropertyNames.removeAll(propertyNames);
        if (!actualPropertyNames.isEmpty()) {
            mismatchDescription.appendText("has extra properties called " + actualPropertyNames);
            return false;
        }
        return true;
    }

    private boolean hasMatchingValues(T item, Description mismatchDescription) {
        for (PropertyMatcher propertyMatcher : propertyMatchers) {
            if (!propertyMatcher.matches(item)) {
                propertyMatcher.describeMismatch(item, mismatchDescription);
                return false;
            }
        }
        return true;
    }

    private List<PropertyMatcher> propertyMatchersFor(T bean,
                                                      PropertyDescriptor[] descriptors) {
        List<PropertyMatcher> result = new ArrayList<PropertyMatcher>(descriptors.length);
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            result.add(new PropertyMatcher(propertyDescriptor, bean));
        }
        return result;
    }

    private Set<String> propertyNamesFrom(PropertyDescriptor[] descriptors) {
        HashSet<String> result = new HashSet<String>();
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            result.add(propertyDescriptor.getDisplayName());
        }
        return result;
    }

    public class PropertyMatcher extends DiagnosingMatcher<Object> {
        private final Method readMethod;
        private final Matcher<Object> matcher;
        private final String propertyName;

        public PropertyMatcher(PropertyDescriptor descriptor, Object expectedObject) {
            this.propertyName = descriptor.getDisplayName();
            this.readMethod = descriptor.getReadMethod();
            // SimpleMatcherのmatchで再帰比較するように
            this.matcher = match(readProperty(readMethod, expectedObject));
        }

        @Override
        public boolean matches(Object actual, Description mismatch) {
            final Object actualValue = readProperty(readMethod, actual);
            if (!matcher.matches(actualValue)) {
                mismatch.appendText(propertyName + " ");
                matcher.describeMismatch(actualValue, mismatch);
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(propertyName + ": ").appendDescriptionOf(matcher);
        }
    }

    private static Object readProperty(Method method, Object target) {
        try {
            return method.invoke(target, NO_ARGUMENTS);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not invoke " + method + " on " + target, e);
        }
    }

    /**
     * Creates a matcher that matches when the examined object has values for all of
     * its JavaBean properties that are equal to the corresponding values of the
     * specified bean.
     * <p/>
     * For example:
     *
     * <pre>
     * assertThat(myBean, samePropertyValuesAs(myExpectedBean))
     * </pre>
     *
     * @param expectedBean the bean against which examined beans are compared
     */
    @Factory
    public static <T> Matcher<T> samePropertyValuesAs(T expectedBean) {
        return new SamePropertyValuesAs<T>(expectedBean);
    }
}
