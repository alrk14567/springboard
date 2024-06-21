<%@page language="java" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>인덱스</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

</head>
<body>

<div class="container-fluid">
    <div class="main h-100">
        <div class="row justify-content-center">
            <div class="col-6 text-center">
                <h1>${message}</h1>
            </div>
        </div>
        <div class="row justify-content-center">
            <div class="col-6 text-center btn btn-outline-danger" onclick="javascript:history.go(-1)">뒤로가기</div>
        </div>
    </div>
</div>
</body>
</html>