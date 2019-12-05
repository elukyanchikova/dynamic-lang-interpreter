var f_a := func(f, x, y) is
    return f(x + y)
end
print f_a

var f_b := func(x) is
    return x*x
end

var result := f_a(f_b, 3, 5)
print result

var array := [1, 2, 3, 5, 8]

var size := 0
var arr_length := func(arr) is
    size := 0
    print "arr_func"
    var executor := func(u) is
        print "executor"
        for iii in loop
            var t := arr[size]
            size := size + 1
        end
    end
    var t := executor(1)
    return size
end

var t := arr_length(array)
print "Array size = ", size



var abc := [111, 5, 4]
var tupling := {a:=125, 1, c:=111}
print abc[0] + abc[1]
var io := 5 + 3
var c := empty
var square := func (x) is
    print x
    return x + x
end
print "strings" + ""
print io * io
print square(io)
var cond := false
var d := 3.65
print io, c, d
var iss
print abc[0]
if cond or false then
    print 1
    print 123
end
for iii in 1 .. 25 loop
    print iii
end
print booleansh


