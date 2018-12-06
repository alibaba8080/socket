# socket
android socket 测试开发
# 引用
> ## 这是一个标题。
> 1. 这是第一行列表项。
>> 2. 这是第二行列表项。
>
> 给出一些例子代码：
>
# 列表
## 无序列表
- Red
- Green
- Blue
## 有序列表
1. Red
    > 代码托管平台  
    > 在线运行环境    
    > 代码质量监控    
    > 项目管理平台
2. Green
3. Blue

## 代办列表

- [ ] 不勾选
- [x] 勾选

# 代码

```ruby
require 'redcarpet'
markdown = Redcarpet.new("Hello World!")
puts markdown.to_html
```

# 强调

## 斜体

*Coding，让开发更简单*  
_Coding，让开发更简单_

## 加粗

**Coding，让开发更简单**  
__Coding，让开发更简单__

# 自动链接

[超强大的云开发平台Coding](http://www.baidu.com)

# 表格