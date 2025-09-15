<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>客户信息填写</title>
    <style>
        .form-container { max-width: 500px; margin: 20px auto; padding: 0 20px; }
        .form-item { margin: 15px 0; }
        label.required::after { content: "*"; color: red; }
        input { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; }
        .submit-btn { width: 100%; padding: 12px; background: #007BFF; color: white; border: none; border-radius: 4px; cursor: pointer; }
        .submit-btn:disabled { background: #6c757d; cursor: not-allowed; }
        .error-msg { color: red; font-size: 14px; }
    </style>
</head>
<body>
<div class="form-container">
    <h2>客户信息收集</h2>
    <form id="scanForm">
        <!-- 隐藏字段：存储二维码参数（activityId、timestamp、sign） -->
        <input type="hidden" id="activityId">
        <input type="hidden" id="timestamp">
        <input type="hidden" id="sign">

        <div class="form-item">
            <label class="required" for="name">姓名</label>
            <input type="text" id="name" placeholder="请输入姓名">
            <div class="error-msg" id="nameError"></div>
        </div>
        <div class="form-item">
            <label class="required" for="phone">手机号</label>
            <input type="tel" id="phone" placeholder="请输入11位手机号">
            <div class="error-msg" id="phoneError"></div>
        </div>
        <div class="form-item">
            <label for="company">公司名称</label>
            <input type="text" id="company" placeholder="可选">
        </div>
        <div class="form-item">
            <label for="position">职位</label>
            <input type="text" id="position" placeholder="可选">
        </div>
        <button type="button" class="submit-btn" id="submitBtn" disabled>提交</button>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script>
    // 解析URL中的二维码参数（activityId、timestamp、sign）
    const urlParams = new URLSearchParams(window.location.search);
    const activityId = urlParams.get('activityId');
    const timestamp = urlParams.get('timestamp');
    const sign = urlParams.get('sign');

    // 初始化隐藏字段（传递给后端校验）
    document.getElementById('activityId').value = activityId;
    document.getElementById('timestamp').value = timestamp;
    document.getElementById('sign').value = sign;

    // 校验参数合法性（防止直接访问页面）
    if (!activityId || !timestamp || !sign) {
        alert('非法访问，请通过活动二维码进入');
        window.location.href = '/crm/index.jsp';
    }

    // 表单实时校验（姓名非空、手机号格式正确）
    const nameInput = document.getElementById('name');
    const phoneInput = document.getElementById('phone');
    const submitBtn = document.getElementById('submitBtn');
    const nameError = document.getElementById('nameError');
    const phoneError = document.getElementById('phoneError');

    function checkFormValid() {
        let isNameValid = nameInput.value.trim() !== '';
        let isPhoneValid = /^1[3-9]\d{9}$/.test(phoneInput.value.trim());

        nameError.textContent = isNameValid ? '' : '姓名不能为空';
        phoneError.textContent = isPhoneValid ? '' : '请输入有效的11位手机号';
        submitBtn.disabled = !(isNameValid && isPhoneValid);
    }

    // 输入变化时触发校验
    nameInput.addEventListener('input', checkFormValid);
    phoneInput.addEventListener('input', checkFormValid);

    // 提交表单到后端
    submitBtn.addEventListener('click', async () => {
        const formData = {
            activityId: document.getElementById('activityId').value,
            timestamp: document.getElementById('timestamp').value,
            sign: document.getElementById('sign').value,
            name: nameInput.value.trim(),
            phone: phoneInput.value.trim(),
            company: document.getElementById('company').value.trim(),
            position: document.getElementById('position').value.trim()
        };

        try {
            submitBtn.disabled = true;
            submitBtn.textContent = '提交中...';
            // 调用后端接口（需与项目部署地址匹配）
            const response = await axios.post(
                '/crm/api/market/customer/scan-submit',
                formData,
                { headers: { 'Content-Type': 'application/json' } }
            );

            if (response.data.code === 200) {
                alert('提交成功！工作人员将尽快联系您');
                document.getElementById('scanForm').reset();
                submitBtn.textContent = '提交';
                submitBtn.disabled = false;
            } else {
                alert('提交失败：' + response.data.msg);
                submitBtn.textContent = '重新提交';
                submitBtn.disabled = false;
            }
        } catch (error) {
            alert('网络异常，请稍后重试');
            submitBtn.textContent = '重新提交';
            submitBtn.disabled = false;
        }
    });
</script>
</body>
</html>