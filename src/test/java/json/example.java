/*
  This file is part of the ONEMA lambda-sample-request Package.
  For the full copyright and license information,
  please view the LICENSE file that was distributed
  with this source code.
  <p>
  copyright (c) 2018, Juan Manuel Torres (http://onema.io)

  @author Juan Manuel Torres <kinojman@gmail.com>
 */

package json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "age",
        "name",
        "blog",
        "messages"
})
class Example {

    @JsonProperty("age")
    private Integer age;
    @JsonProperty("name")
    private String name;
    @JsonProperty("blog")
    private String blog;
    @JsonProperty("messages")
    private List<String> messages = null;

    @JsonProperty("age")
    public Integer getAge() {
        return age;
    }

    @JsonProperty("age")
    public void setAge(Integer age) {
        this.age = age;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("blog")
    public String getBlog() {
        return blog;
    }

    @JsonProperty("blog")
    public void setBlog(String blog) {
        this.blog = blog;
    }

    @JsonProperty("messages")
    public List<String> getMessages() {
        return messages;
    }

    @JsonProperty("messages")
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
