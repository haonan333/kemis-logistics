package cn.kemis

import cn.kemis.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification
import spock.lang.Unroll

/**
 *
 * @author 刘体阳 jefferlzu@gmail.com
 * Created on 2016-08-28
 */
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImportUser extends Specification {

    @Autowired
    private UserService userService

    @Autowired
    WebApplicationContext context

    def "should boot up without errors"() {
        expect: "Web context exists"
        context != null
    }

    @Unroll
    def "max(#a,#b) == #c"() {
        expect:
        // This class is in our Java code
        ImportUser.max(a, b) == c

        where:
        a  | b   | c
        1  | 2   | 2
        42 | -12 | 42
        42 | -12 | -42
    }

    static max(a, b) {
        if (a > b) {
            a
        } else {
            b
        }
    }
}
