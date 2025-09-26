<template>
  <div class="marketing-layout">
    <!-- 侧边栏 -->
    <div :class="['sidebar', { collapsed: isSidebarCollapsed }]">
      <div class="sidebar-header">
        <h3 v-if="!isSidebarCollapsed">营销活动</h3>
        <el-button
          class="toggle-btn"
          @click="toggleSidebar"
          :icon="isSidebarCollapsed ? 'Expand' : 'Fold'"
          circle
        />
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isSidebarCollapsed"
        @select="handleMenuSelect"
      >
        <el-menu-item index="promotion">
          <el-icon><Calendar /></el-icon>
          <template #title>活动策划</template>
        </el-menu-item>
        <el-menu-item index="activity">
          <el-icon><TrendCharts /></el-icon>
          <template #title>活动实施</template>
        </el-menu-item>
        <el-menu-item index="report">
          <el-icon><Document /></el-icon>
          <template #title>活动报告</template>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 主内容区域 -->
    <div :class="['main-content', { collapsed: isSidebarCollapsed }]">
      <!-- 活动策划模块 -->
      <div v-if="activeMenu === 'promotion'" class="promotion-container">
        <div class="header">
          <h2>活动策划管理</h2>
          <el-button type="primary" @click="handleAddPlan">新增策划</el-button>
        </div>

        <!-- 策划列表 -->
        <el-table :data="promotionPlans" border class="plans-table">
          <el-table-column prop="id" label="ID" width="60" align="center"/>
          <el-table-column prop="title" label="策划名称" min-width="150" align="center"/>
          <el-table-column label="策划描述" min-width="120" align="center">
            <template #default="{ row }">
              <el-button size="small" type="primary" plain @click="viewPlanContent(row)">
                查看详情
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="budget" label="预算" width="100" align="center">
            <template #default="{ row }">
              ¥{{ parseFloat(row.budget).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="planStatusName" label="状态" width="100" align="center">
            <template #default="{ row }">
              <el-tag :type="getPlanStatusTagTypeByName(row.planStatusName)">
                {{ row.planStatusName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="submitterName" label="提交人" width="100" align="center"/>
          <el-table-column prop="approverName" label="审批人" width="100" align="center"/>
          <el-table-column prop="createTime" label="创建时间" width="120" align="center"/>
          <el-table-column prop="updateTime" label="更新时间" width="120" align="center"/>
          <el-table-column label="操作" width="150" fixed="right" align="center">
            <template #default="{ row }">
              <el-button size="small" @click="handleViewPlan(row)">查看</el-button>
              <el-button
                size="small"
                type="primary"
                :disabled="row.planStatusName !== '待审批'"
                @click="handleApprovePlan(row)"
                v-if="permissionStore.hasButtonPermission('/marketActivity/approve')"
              >
                审批
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 策划对话框 -->
        <el-dialog
          v-model="planDialogVisible"
          title="新增活动策划"
          width="600px"
          @close="onPlanDialogClose"
        >
          <el-form :model="currentPlan" label-width="100px" class="dialog-form">
            <el-form-item label="策划名称" prop="title" :rules="[{ required: true, message: '请输入策划名称', trigger: 'blur' }]">
              <el-input v-model="currentPlan.title" placeholder="请输入策划名称"/>
            </el-form-item>
            <el-form-item label="策划描述" prop="content">
              <el-input
                v-model="currentPlan.content"
                type="textarea"
                placeholder="请输入策划描述"
                :rows="3"
              />
            </el-form-item>
            <el-form-item label="预算" prop="budget">
              <el-input-number
                v-model="currentPlan.budget"
                :min="0"
                :precision="2"
                :step="1000"
                style="width: 100%"
              />
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="planDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="savePlan">确认</el-button>
            </span>
          </template>
        </el-dialog>

        <!-- 策划详情对话框 -->
        <el-dialog v-model="planDetailDialogVisible" title="策划详情" width="600px">
          <el-form label-width="120px" label-position="left" class="detail-form">
            <el-row>
              <el-col :span="12">
                <el-form-item label="策划ID:">
                  <span>{{ detailPlan.id }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="策划名称:">
                  <span>{{ detailPlan.title }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="预算:">
                  <span>¥{{ parseFloat(detailPlan.budget).toFixed(2) }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="状态:">
                  <el-tag :type="getPlanStatusTagTypeByName(detailPlan.planStatusName)">
                    {{ detailPlan.planStatusName }}
                  </el-tag>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="提交人:">
                  <span>{{ detailPlan.submitterName }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="审批人:">
                  <span>{{ detailPlan.approverName || '未审批' }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="审批反馈:">
              <span>{{ detailPlan.feedback || '无' }}</span>
            </el-form-item>
            <el-form-item label="创建时间:">
              <span>{{ detailPlan.createTime }}</span>
            </el-form-item>
            <el-form-item label="更新时间:">
              <span>{{ detailPlan.updateTime }}</span>
            </el-form-item>
          </el-form>
        </el-dialog>

        <!-- 策划描述详情对话框（参考报告内容展示方式） -->
        <el-dialog v-model="planContentDialogVisible" title="策划描述详情" width="800px" top="5vh">
          <el-scrollbar max-height="70vh">
            <div class="plan-content-section">
              <h3>策划描述</h3>
              <div class="plan-content-html" v-html="formatPlanContent(detailPlan.content)"></div>
            </div>
          </el-scrollbar>

          <template #footer>
            <div class="dialog-footer">
              <el-button @click="planContentDialogVisible = false">关闭</el-button>
            </div>
          </template>
        </el-dialog>

        <!-- 审批对话框 -->
        <el-dialog
          v-model="approveDialogVisible"
          :title="approveDialogTitle"
          width="500px"
          @close="onApproveDialogClose"
        >
          <el-form :model="approveForm" label-width="80px" class="dialog-form">
            <el-form-item label="审批结果" prop="status" :rules="[{ required: true, message: '请选择审批结果', trigger: 'change' }]">
              <el-select v-model="approveForm.status" placeholder="请选择审批结果" style="width: 100%">
                <el-option label="通过" :value="1"/>
                <el-option label="驳回" :value="2"/>
              </el-select>
            </el-form-item>
            <el-form-item label="审批意见" prop="feedback">
              <el-input
                v-model="approveForm.feedback"
                type="textarea"
                placeholder="请输入审批意见"
                :rows="3"
              />
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="approveDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="submitApproval">确认</el-button>
            </span>
          </template>
        </el-dialog>
      </div>

      <!-- 活动实施模块 -->
      <div v-if="activeMenu === 'activity'" class="marketing-container">
        <div class="header">
          <h2>营销活动管理</h2>
          <el-button type="primary" @click="handleAdd">新增活动</el-button>
        </div>

        <!-- 搜索区域 -->
        <el-form :inline="true" :model="searchForm" class="search-form" label-position="left">
          <el-form-item label="活动名称" label-width="80px">
            <el-input
              v-model="searchForm.name"
              placeholder="输入活动名称"
              clearable
              size="small"
            />
          </el-form-item>
          <el-form-item label="活动状态" label-width="80px">
            <el-select
              v-model="searchForm.status"
              placeholder="请选择"
              clearable
              size="small"
            >
              <el-option label="筹备中" value="1"/>
              <el-option label="进行中" value="2"/>
              <el-option label="已结束" value="3"/>
              <el-option label="已取消" value="4"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="fetchActivities" size="small">搜索</el-button>
            <el-button @click="resetSearch" size="small">重置</el-button>
          </el-form-item>
        </el-form>

        <!-- 活动表格 -->
        <el-table :data="activities" border class="activities-table">
          <el-table-column prop="id" label="ID" width="60" align="center"/>
          <el-table-column prop="name" label="活动名称" min-width="150" align="center"/>
          <el-table-column label="活动描述" min-width="200" align="center">
            <template #default="{ row }">
              <el-button size="small" type="primary" plain @click="viewActivityContent(row)">
                查看详情
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="startTime" label="开始时间" width="120" align="center"/>
          <el-table-column prop="endTime" label="结束时间" width="120" align="center"/>

          <!-- 状态列 - 不同状态显示不同颜色 -->
          <el-table-column label="状态" width="100" align="center">
            <template #default="{ row }">
              <span :class="getActivityStatusClass(Number(row.activityStatus))">
                {{ row.activityStatusName || getActivityStatusText(row.activityStatus) }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="estimatedCost" label="预算" width="100" align="center">
            <template #default="{ row }">
              ¥{{ parseFloat(row.estimatedCost).toFixed(2) }}
            </template>
          </el-table-column>

          <el-table-column prop="actualCost" label="实际花费" width="100" align="center">
            <template #default="{ row }">
              ¥{{ parseFloat(row.actualCost).toFixed(2) }}
            </template>
          </el-table-column>

          <el-table-column label="操作" width="150" fixed="right" align="center">
            <template #default="{ row }">
              <el-button size="small" @click="handleDetail(row)">详情</el-button>
              <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          @current-change="handleCurrentChange"
          layout="total, prev, pager, next"
          class="pagination"
        />

        <!-- 活动对话框 -->
        <el-dialog
          v-model="dialogVisible"
          :title="dialogTitle"
          width="600px"
          @close="onDialogClose"
        >
          <el-form :model="currentActivity" label-width="100px" class="dialog-form">
            <el-form-item label="活动名称" prop="name" :rules="[{ required: true, message: '请输入活动名称', trigger: 'blur' }]">
              <el-input v-model="currentActivity.name" placeholder="请输入活动名称"/>
            </el-form-item>
            <el-form-item label="关联方案ID" prop="planId">
              <el-input v-model.number="currentActivity.planId" placeholder="请输入关联方案ID" type="number"/>
            </el-form-item>
            <el-form-item label="活动描述" prop="content">
              <el-input
                v-model="currentActivity.content"
                type="textarea"
                placeholder="请输入活动描述"
                :rows="3"
              />
            </el-form-item>
            <el-form-item label="活动地点" prop="location">
              <el-input v-model="currentActivity.location" placeholder="请输入活动地点"/>
            </el-form-item>
            <el-form-item label="活动形式" prop="form">
              <el-input v-model="currentActivity.form" placeholder="请输入活动形式"/>
            </el-form-item>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="开始时间" prop="startTime" :rules="[{ required: true, message: '请选择开始时间', trigger: 'change' }]">
                  <el-date-picker
                    v-model="currentActivity.startTime"
                    type="date"
                    placeholder="请选择开始时间"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="结束时间" prop="endTime" :rules="[{ required: true, message: '请选择结束时间', trigger: 'change' }]">
                  <el-date-picker
                    v-model="currentActivity.endTime"
                    type="date"
                    placeholder="请选择结束时间"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="预算" prop="estimatedCost">
                  <el-input-number
                    v-model="currentActivity.estimatedCost"
                    :min="0"
                    :precision="2"
                    :step="1000"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="实际花费" prop="actualCost">
                  <el-input-number
                    v-model="currentActivity.actualCost"
                    :min="0"
                    :precision="2"
                    :step="1000"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="活动状态" prop="activityStatus" :rules="[{ required: true, message: '请选择活动状态', trigger: 'change' }]">
              <el-select v-model="currentActivity.activityStatus" placeholder="请选择活动状态" style="width: 100%">
                <el-option label="筹备中" :value="1"/>
                <el-option label="进行中" :value="2"/>
                <el-option label="已结束" :value="3"/>
                <el-option label="已取消" :value="4"/>
              </el-select>
            </el-form-item>
            <el-form-item label="负责人ID" prop="managerId" :rules="[{ required: true, message: '请输入负责人ID', trigger: 'blur' }]">
              <el-input v-model.number="currentActivity.managerId" placeholder="请输入负责人ID" type="number"/>
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="cancelUpdate">取消</el-button>
              <el-button type="primary" @click="saveActivity">确认</el-button>
            </span>
          </template>
        </el-dialog>

        <!-- 活动详情对话框 -->
        <el-dialog v-model="detailDialogVisible" title="活动详情" width="600px">
          <el-form label-width="120px" label-position="left" class="detail-form">
            <el-row>
              <el-col :span="12">
                <el-form-item label="活动ID:">
                  <span>{{ detailActivity.id }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="活动名称:">
                  <span>{{ detailActivity.name }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="活动描述:">
              <span>{{ detailActivity.content || '无' }}</span>
            </el-form-item>
            <el-row>
              <el-col :span="12">
                <el-form-item label="开始时间:">
                  <span>{{ detailActivity.startTime }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="结束时间:">
                  <span>{{ detailActivity.endTime }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="预算:">
                  <span>¥{{ parseFloat(detailActivity.estimatedCost).toFixed(2) }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="实际花费:">
                  <span>¥{{ parseFloat(detailActivity.actualCost).toFixed(2) }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="活动状态:">
              <span :class="getActivityStatusClass(Number(detailActivity.activityStatus))">
                {{ detailActivity.activityStatusName || getActivityStatusText(detailActivity.activityStatus) }}
              </span>
            </el-form-item>
            <el-form-item label="负责人:">
              <span>{{ detailActivity.managername }}</span>
            </el-form-item>
            <el-form-item label="二维码:">
              <div class="qr-code-container">
                <div class="qr-code-row">
                  <div class="qr-code-item">
                    <div v-if="detailActivity.id && detailActivity.managerId" class="qr-code-wrapper">
                      <img
                        :src="commonQRCodeUrl"
                        @error="handleQRCodeError($event, 'common')"
                        @load="handleQRCodeLoad($event, 'common')"
                        alt="普通活动二维码"
                        class="qr-code-image"
                        style="max-width: 100%; height: auto; cursor: pointer;"
                        @click="showLargeQRCode"
                      />
                    </div>
                    <div v-else class="qr-code-placeholder">
                      <p>活动信息不完整，无法生成二维码</p>
                    </div>
                  </div>
                </div>

                <p v-if="(detailActivity.id && detailActivity.managerId) && qrCodeErrors.common" class="qr-code-error">
                  二维码加载失败，请检查网络连接或联系管理员
                </p>
              </div>

              <!-- 大的二维码预览对话框 -->
              <el-dialog
                v-model="largeQRCodeVisible"
                title="二维码预览"
                width="500px"
                center
              >
                <div class="large-qr-code-container">
                  <img
                    :src="commonQRCodeUrl"
                    alt="大尺寸二维码"
                    class="large-qr-code-image"
                  />
                </div>
              </el-dialog>
            </el-form-item>
            <el-form-item label="创建时间:">
              <span>{{ detailActivity.createTime }}</span>
            </el-form-item>
          </el-form>
        </el-dialog>

        <!-- 活动描述详情对话框（参考报告内容展示方式） -->
        <el-dialog
          v-model="activityContentDialogVisible"
          title="活动描述详情"
          width="800px"
          top="5vh"
          :modal="true"
          :close-on-click-modal="true"
          :show-close="true"
        >
          <el-scrollbar max-height="70vh">
            <div class="activity-content-section">
              <h3>活动描述</h3>
              <div class="activity-content-html" v-html="formatActivityContent(detailActivity.content)"></div>
            </div>
          </el-scrollbar>

          <template #footer>
            <div class="dialog-footer">
              <el-button @click="activityContentDialogVisible = false">关闭</el-button>
            </div>
          </template>
        </el-dialog>
      </div>

      <!-- 活动报告模块 -->
      <div v-if="activeMenu === 'report'" class="report-container">
        <div class="header">
          <h2>活动报告管理</h2>
          <el-button type="primary" @click="handleAddReport">新增报告</el-button>
        </div>

        <!-- 报告列表 -->
        <el-table :data="activityReports" border class="reports-table">
          <el-table-column prop="id" label="ID" width="60" align="center"/>
          <el-table-column prop="title" label="报告标题" min-width="150" align="center"/>
          <el-table-column prop="activityId" label="关联活动ID" width="120" align="center"/>
          <el-table-column prop="actualCost" label="实际成本" width="100" align="center">
            <template #default="{ row }">
              ¥{{ parseFloat(row.actualCost).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="newCustomersCount" label="新增客户数" width="120" align="center"/>
          <el-table-column prop="potentialLeadsCount" label="潜在线索数" width="120" align="center"/>
          <el-table-column prop="estimatedRoi" label="预估ROI" width="100" align="center">
            <template #default="{ row }">
              {{ (row.estimatedRoi * 100).toFixed(2) }}%
            </template>
          </el-table-column>
          <el-table-column prop="creatorName" label="创建人" width="100" align="center"/>
          <el-table-column prop="createTime" label="创建时间" width="120" align="center"/>
          <!-- 报告操作栏 -->
          <el-table-column label="报告内容" width="120" fixed="right" align="center">
            <template #default="{ row }">
              <el-button
                size="small"
                type="primary"
                plain
                @click="viewReportDetail(row)"
              >
                查看详情
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right" align="center">
            <template #default="{ row }">
              <el-button size="small" @click="handleEditReport(row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDeleteReport(row)" v-if="permissionStore.hasButtonPermission('/activityReport/delete')">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 报告对话框 -->
        <el-dialog
          v-model="reportDialogVisible"
          :title="reportDialogTitle"
          width="600px"
          @close="onReportDialogClose"
        >
          <el-form :model="currentReport" label-width="120px" class="dialog-form">
            <el-form-item label="报告标题" prop="title" :rules="[{ required: true, message: '请输入报告标题', trigger: 'blur' }]">
              <el-input v-model="currentReport.title" placeholder="请输入报告标题"/>
            </el-form-item>
            <el-form-item label="关联活动ID" prop="activityId" :rules="[{ required: true, message: '请输入关联活动ID', trigger: 'blur' }]">
              <el-input v-model="currentReport.activityId" placeholder="请输入关联活动ID"/>
            </el-form-item>
            <el-form-item label="报告内容" prop="content">
              <el-input
                v-model="currentReport.content"
                type="textarea"
                placeholder="请输入报告内容"
                :rows="4"
              />
            </el-form-item>
            <el-form-item label="实际成本" prop="actualCost">
              <el-input-number
                v-model="currentReport.actualCost"
                :min="0"
                :precision="2"
                :step="1000"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="新增客户数" prop="newCustomersCount">
              <el-input-number
                v-model="currentReport.newCustomersCount"
                :min="0"
                :step="1"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="潜在线索数" prop="potentialLeadsCount">
              <el-input-number
                v-model="currentReport.potentialLeadsCount"
                :min="0"
                :step="1"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="预估ROI" prop="estimatedRoi">
              <el-input-number
                v-model="currentReport.estimatedRoi"
                :min="0"
                :max="100"
                :precision="4"
                :step="0.01"
                style="width: 100%"
              />
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="reportDialogVisible = false">取消</el-button>
              <el-button type="primary" @click="saveReport">确认</el-button>
            </span>
          </template>
        </el-dialog>

        <!-- 报告详情对话框 -->
        <el-dialog v-model="reportDetailDialogVisible" title="报告详情" width="800px" top="5vh">
          <el-scrollbar max-height="70vh">
            <div class="report-detail-content">
              <el-descriptions :column="2" border>
                <el-descriptions-item label="报告ID">{{ detailReport.id }}</el-descriptions-item>
                <el-descriptions-item label="报告标题">{{ detailReport.title }}</el-descriptions-item>
                <el-descriptions-item label="关联活动ID">{{ detailReport.activityId }}</el-descriptions-item>
                <el-descriptions-item label="实际成本">
                  ¥{{ parseFloat(detailReport.actualCost).toFixed(2) }}
                </el-descriptions-item>
                <el-descriptions-item label="新增客户数">{{ detailReport.newCustomersCount }}</el-descriptions-item>
                <el-descriptions-item label="潜在线索数">{{ detailReport.potentialLeadsCount }}</el-descriptions-item>
                <el-descriptions-item label="预估ROI">
                  {{ (detailReport.estimatedRoi * 100).toFixed(2) }}%
                </el-descriptions-item>
                <el-descriptions-item label="创建人">{{ detailReport.creatorName }}</el-descriptions-item>
                <el-descriptions-item label="创建时间" :span="2">{{ detailReport.createTime }}</el-descriptions-item>
              </el-descriptions>

              <div class="report-content-section">
                <h3>报告内容</h3>
                <div class="report-content-html" v-html="formatReportContent(detailReport.content)"></div>
              </div>
            </div>
          </el-scrollbar>

          <template #footer>
            <div class="dialog-footer">
              <el-button @click="reportDetailDialogVisible = false">关闭</el-button>
            </div>
          </template>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { useRouter } from 'vue-router'
import { Calendar, TrendCharts, Document, Expand, Fold } from '@element-plus/icons-vue'

import { usePermissionStore } from '@/stores/permission'
const permissionStore = usePermissionStore()

// 获取路由实例
const router = useRouter()

// 侧边栏状态
const isSidebarCollapsed = ref(false)
const activeMenu = ref('promotion') // 默认显示活动策划模块

// 活动策划相关数据
const promotionPlans = ref([])
const planDialogVisible = ref(false)
const planDetailDialogVisible = ref(false)
const planContentDialogVisible = ref(false) // 新增的策划描述详情对话框
const approveDialogVisible = ref(false)
const approveDialogTitle = ref('审批策划')
const approveForm = ref({
  status: 1,
  feedback: ''
})
const currentApprovePlan = ref({})

const currentPlan = ref({
  title: '',
  content: '',
  budget: 0,
  status: 0, // 0: 待审批, 1: 已批准, 2: 已拒绝
  submitterId: 1, // 示例值，实际应从用户信息中获取
  approverId: null,
  feedback: '',
  createTime: '',
  updateTime: ''
})
const detailPlan = ref({})

// 活动实施相关数据
const activities = ref([])
const searchForm = ref({
  name: '',
  status: ''
})
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})
const dialogVisible = ref(false)
const dialogTitle = ref('新增活动')
const currentActivity = ref({
  name: '',
  planId: null,
  content: '',
  location: '',
  form: '',
  startTime: '',
  endTime: '',
  estimatedCost: 0,
  actualCost: 0,
  activityStatus: 1, // 默认筹备中
  managerId: 1 // 示例值，实际应从用户信息中获取
})
const detailDialogVisible = ref(false)
const activityContentDialogVisible = ref(false) // 确保这个变量已定义
const detailActivity = ref({})

// 活动报告相关数据
const activityReports = ref([])
const reportDialogVisible = ref(false)
const reportDetailDialogVisible = ref(false)
const reportDialogTitle = ref('新增报告')
const currentReport = ref({
  title: '',
  activityId: '',
  content: '',
  actualCost: 0,
  newCustomersCount: 0,
  potentialLeadsCount: 0,
  estimatedRoi: 0,
  creatorId: 1 // 示例值，实际应从用户信息中获取
})
const detailReport = ref({})

// 添加API基础URL
const API_BASE_URL = 'http://localhost:8080';

// 计算二维码URL
const commonQRCodeUrl = computed(() => {
  if (detailActivity.value.id && detailActivity.value.managerId) {
    const url = `${API_BASE_URL}/market/qrcode/common?activityId=${detailActivity.value.id}&creatorId=${detailActivity.value.managerId}`;
    console.log('普通二维码URL:', url);
    return url;
  }
  return '';
});

// 添加大二维码预览相关数据
const largeQRCodeVisible = ref(false);

// 显示大二维码预览
const showLargeQRCode = () => {
  if (commonQRCodeUrl.value) {
    largeQRCodeVisible.value = true;
  }
};

// 添加二维码相关数据
const qrCodeErrors = ref({
  common: false,
  logo: false
})

// 处理二维码加载错误
const handleQRCodeError = (event, type) => {
  console.error(`二维码加载失败 (${type}):`, event);
  console.error('失败的二维码URL:', event.target.src);
  qrCodeErrors.value[type] = true;
};

// 处理二维码加载成功
const handleQRCodeLoad = (event, type) => {
  console.log(`二维码加载成功 (${type}):`, event);
  console.log('加载成功的二维码URL:', event.target.src);
  qrCodeErrors.value[type] = false;
};

// 切换侧边栏
const toggleSidebar = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value
}

// 菜单选择
const handleMenuSelect = (index) => {
  activeMenu.value = index
  if (index === 'promotion') {
    fetchPromotionPlans()
  } else if (index === 'activity') {
    fetchActivities()
  } else if (index === 'report') {
    fetchActivityReports()
    fetchActivities() // 获取活动列表用于报告关联
  }
}

// 获取活动策划列表
const fetchPromotionPlans = async () => {
  try {
    const response = await request.get('/marketing/getPromotionPlans')
    console.log('策划接口响应:', response)

    // 直接使用响应数据，不再检查code
    if (response && response.data) {
      // 检查response.data是否直接就是数组
      if (Array.isArray(response.data)) {
        promotionPlans.value = response.data
        ElMessage.success(`策划数据加载成功，共 ${response.data.length} 条数据`)
      }
      // 检查response.data中是否有data字段（后端R类封装的格式）
      else if (response.data.data && Array.isArray(response.data.data)) {
        promotionPlans.value = response.data.data
        ElMessage.success(`策划数据加载成功，共 ${response.data.data.length} 条数据`)
      }
      // 检查是否是后端直接返回的列表（没有封装在data中）
      else {
        promotionPlans.value = []
        ElMessage.warning('策划数据格式不正确')
      }
    } else {
      promotionPlans.value = []
      ElMessage.info('暂无策划数据')
    }
  } catch (error) {
    console.error('获取活动策划失败:', error)
    promotionPlans.value = []
    ElMessage.error('获取活动策划失败: ' + (error.message || '网络错误'))
  }
}

// 获取活动列表
const fetchActivities = async () => {
  try {
    let response;

    // 如果有任何搜索条件，使用组合查询接口
    if (searchForm.value.name || searchForm.value.status !== '') {
      // 准备查询参数
      const params = {};

      if (searchForm.value.name) {
        params.name = searchForm.value.name.trim();
      }

      if (searchForm.value.status !== '') {
        params.status = searchForm.value.status;
      }

      // 如果有状态筛选，使用状态查询接口
      if (searchForm.value.status !== '') {
        response = await request.get('/marketActivity/getByStatus', {
          params: { status: searchForm.value.status }
        });
      }
      // 如果只有名称筛选，使用名称查询接口
      else if (searchForm.value.name) {
        response = await request.get('/marketActivity/getByName', {
          params: { name: searchForm.value.name }
        });
      }
    }
    // 默认查询所有活动
    else {
      response = await request.get('/marketActivity/getAll');
    }

    // 处理响应数据
    console.log('活动接口响应:', response)
    if (response && response.data) {
      let data = [];

      // 检查response.data是否直接就是数组
      if (Array.isArray(response.data)) {
        data = response.data;
      }
      // 检查response.data中是否有data字段（后端R类封装的格式）
      else if (response.data.data && Array.isArray(response.data.data)) {
        data = response.data.data;
      }

      activities.value = data;
      pagination.value.total = data.length;

      if (data.length > 0) {
        ElMessage.success(`数据加载成功，共 ${data.length} 条记录`);
      } else {
        ElMessage.info('暂无活动数据');
      }
    } else {
      activities.value = [];
      pagination.value.total = 0;
      ElMessage.info('暂无活动数据');
    }

    console.log('设置的activities数据:', activities.value);
  } catch (error) {
    console.error('请求异常:', error);
    activities.value = [];
    pagination.value.total = 0;
    ElMessage.error('请求失败: ' + (error.message || '网络错误'));
  }
}

// 获取活动报告列表
const fetchActivityReports = async () => {
  try {
    const response = await request.get('/activityReport/getAll')
    console.log('报告接口响应:', response)

    // 直接使用响应数据，不再检查code
    if (response && response.data) {
      // 检查response.data是否直接就是数组
      if (Array.isArray(response.data)) {
        activityReports.value = response.data
        ElMessage.success(`报告数据加载成功，共 ${response.data.length} 条数据`)
      }
      // 检查response.data中是否有data字段（后端R类封装的格式）
      else if (response.data.data && Array.isArray(response.data.data)) {
        activityReports.value = response.data.data
        ElMessage.success(`报告数据加载成功，共 ${response.data.data.length} 条数据`)
      }
      // 检查是否是后端直接返回的列表（没有封装在data中）
      else {
        activityReports.value = []
        ElMessage.warning('报告数据格式不正确')
      }
    } else {
      activityReports.value = []
      ElMessage.info('暂无报告数据')
    }
  } catch (error) {
    console.error('获取活动报告失败:', error)
    activityReports.value = []
    ElMessage.error('获取活动报告失败: ' + (error.message || '网络错误'))
  }
}

// 活动策划相关方法
const handleAddPlan = () => {
  const now = formatDate(new Date());
  currentPlan.value = {
    title: '',
    content: '',
    budget: 0,
    status: 0,
    submitterId: 1, // 示例值，实际应从用户信息中获取
    approverId: null,
    feedback: '',
    createTime: now,
    updateTime: now
  }
  planDialogVisible.value = true
}

// 格式化日期为 MySQL 兼容格式
const formatDate = (date) => {
  if (!date) return '';
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  const hours = String(d.getHours()).padStart(2, '0');
  const minutes = String(d.getMinutes()).padStart(2, '0');
  const seconds = String(d.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

const savePlan = async () => {
  if (!currentPlan.value.title) {
    ElMessage.warning('请输入策划名称')
    return
  }

  try {
    // 确保日期格式正确
    const planData = {
      ...currentPlan.value,
      createTime: formatDate(currentPlan.value.createTime),
      updateTime: formatDate(currentPlan.value.updateTime)
    };

    const response = await request.put('/marketing/savePromotionPlans', planData)
    console.log('新增策划响应:', response)

    // 检查响应状态
    if (response && (response.code === 200 || response.status === 200)) {
      ElMessage.success(response.message || response.data || '新增策划成功')
      planDialogVisible.value = false
      fetchPromotionPlans()
    } else {
      ElMessage.error(response.message || response.data || '新增策划失败')
    }
  } catch (error) {
    console.error('新增策划失败:', error)
    ElMessage.error('新增策划失败: ' + (error.message || '网络错误'))
  }
}

const handleViewPlan = (row) => {
  detailPlan.value = { ...row }
  planDetailDialogVisible.value = true
}

// 查看策划描述详情（参考报告内容展示方式）
const viewPlanContent = (row) => {
  detailPlan.value = { ...row }
  planContentDialogVisible.value = true
}

// 处理审批
const handleApprovePlan = (row) => {
  currentApprovePlan.value = { ...row }
  approveDialogTitle.value = `审批策划 - ${row.title}`
  approveForm.value = {
    status: 1,
    feedback: row.feedback || ''
  }
  approveDialogVisible.value = true
}

// 提交审批
const submitApproval = async () => {
  try {
    const approveData = {
      ...currentApprovePlan.value,
      status: approveForm.value.status,
      feedback: approveForm.value.feedback,
      approverId: 1, // 示例值，实际应从用户信息中获取
      updateTime: formatDate(new Date())
    }

    const response = await request.put('/marketing/updatePromotionPlans', approveData)
    console.log('审批响应:', response)

    // 检查响应状态
    if (response && (response.code === 200 || response.status === 200)) {
      ElMessage.success(response.message || response.data || '审批成功')
      approveDialogVisible.value = false
      fetchPromotionPlans()
    } else {
      ElMessage.error(response.message || response.data || '审批失败')
    }
  } catch (error) {
    console.error('审批失败:', error)
    ElMessage.error('审批失败: ' + (error.message || '网络错误'))
  }
}

const onApproveDialogClose = () => {
  approveDialogVisible.value = false
}

const onPlanDialogClose = () => {
  planDialogVisible.value = false
}

// 活动实施相关方法
const resetSearch = () => {
  searchForm.value = {
    name: '',
    status: ''
  }
  fetchActivities()
}

const handleDetail = (row) => {
  detailActivity.value = {...row}
  detailDialogVisible.value = true
}

const handleAdd = () => {
  dialogTitle.value = '新增活动'
  currentActivity.value = {
    name: '',
    planId: null,
    content: '',
    location: '',
    form: '',
    startTime: '',
    endTime: '',
    estimatedCost: 0,
    actualCost: 0,
    activityStatus: 1, // 默认筹备中
    managerId: 1 // 示例值，实际应从用户信息中获取
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑活动'
  currentActivity.value = {...row}
  dialogVisible.value = true
}

const saveActivity = async () => {
  if (!currentActivity.value.name) {
    ElMessage.warning('请输入活动名称');
    return;
  }

  if (!currentActivity.value.startTime || !currentActivity.value.endTime) {
    ElMessage.warning('请选择开始时间和结束时间');
    return;
  }

  if (!currentActivity.value.activityStatus) {
    ElMessage.warning('请选择活动状态');
    return;
  }

  if (!currentActivity.value.managerId) {
    ElMessage.warning('请输入负责人ID');
    return;
  }

  try {
    let response;

    // 格式化日期为 MySQL 兼容格式
    const formatDateTime = (date) => {
      if (!date) return '';
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      const hours = String(d.getHours()).padStart(2, '0');
      const minutes = String(d.getMinutes()).padStart(2, '0');
      const seconds = String(d.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    };

    // 准备发送给后端的数据，确保字段名与Mapper文件完全匹配，并且类型正确
    const activityData = {
      id: currentActivity.value.id || undefined,
      name: currentActivity.value.name,
      planId: currentActivity.value.planId ? parseInt(currentActivity.value.planId) : null,
      content: currentActivity.value.content || '',
      location: currentActivity.value.location || '',
      form: currentActivity.value.form || '',
      startTime: currentActivity.value.startTime ? formatDateTime(currentActivity.value.startTime) : '',
      endTime: currentActivity.value.endTime ? formatDateTime(currentActivity.value.endTime) : '',
      estimatedCost: parseFloat(currentActivity.value.estimatedCost) || 0,
      actualCost: parseFloat(currentActivity.value.actualCost) || 0,
      managerId: parseInt(currentActivity.value.managerId),
      activityStatus: parseInt(currentActivity.value.activityStatus),
      createTime: currentActivity.value.id ? currentActivity.value.createTime : formatDateTime(new Date())
    };

    console.log('发送的活动数据:', activityData);

    // 如果有ID则为更新，否则为新增
    if (currentActivity.value.id) {
      response = await request.put('/marketActivity/update', activityData);
    } else {
      // 新增活动，使用URLSearchParams格式发送
      const params = new URLSearchParams();
      for (const key in activityData) {
        if (activityData[key] !== null && activityData[key] !== undefined) {
          params.append(key, activityData[key]);
        }
      }

      response = await request.post('/marketActivity/add', params, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      });
    }

    console.log('保存活动响应:', response);
    // 检查响应结果
    if (response && (response.code === 200 || response.status === 200)) {
      ElMessage.success(response.message || response.data || (currentActivity.value.id ? '更新成功' : '新增成功'));
      dialogVisible.value = false;
      fetchActivities(); // 重新加载数据
    } else {
      ElMessage.error(response.message || response.data || (currentActivity.value.id ? '更新失败' : '新增失败'));
    }
  } catch (error) {
    console.error('完整错误:', error);

    // 更详细地处理错误信息
    if (error.response && error.response.data) {
      const errorData = error.response.data;
      if (errorData.message) {
        ElMessage.error('操作失败: ' + errorData.message);
      } else {
        ElMessage.error('操作失败: ' + (error.message || '服务器内部错误'));
      }
    } else if (error.message && error.message.includes('未登录')) {
      ElMessage.error('请先登录');
      router.push('/login');
    } else if (error.message && error.message.includes('Token')) {
      ElMessage.error('身份验证已过期，请重新登录');
      localStorage.removeItem('crm_token');
      router.push('/login');
    } else {
      ElMessage.error((currentActivity.value.id ? '更新失败' : '新增失败') + ': ' + (error.message || '网络错误'));
    }
  }
}

const cancelUpdate = () => {
  dialogVisible.value = false
}

const onDialogClose = () => {
  cancelUpdate();
}

const handleCurrentChange = (val) => {
  pagination.value.current = val;
  fetchActivities();
}

// 活动报告相关方法
const handleAddReport = () => {
  reportDialogTitle.value = '新增报告'
  currentReport.value = {
    title: '',
    activityId: '',
    content: '',
    actualCost: 0,
    newCustomersCount: 0,
    potentialLeadsCount: 0,
    estimatedRoi: 0,
    creatorId: 1 // 示例值，实际应从用户信息中获取
  }
  reportDialogVisible.value = true
}

const handleEditReport = (row) => {
  reportDialogTitle.value = '编辑报告'
  currentReport.value = { ...row }
  reportDialogVisible.value = true
}

const saveReport = async () => {
  if (!currentReport.value.title) {
    ElMessage.warning('请输入报告标题')
    return
  }

  if (!currentReport.value.activityId) {
    ElMessage.warning('请输入关联活动ID')
    return
  }

  try {
    let response
    if (currentReport.value.id) {
      // 更新报告
      response = await request.put('/activityReport/update', currentReport.value)
    } else {
      // 新增报告
      // 设置创建时间
      currentReport.value.createTime = formatDate(new Date());
      response = await request.post('/activityReport/add', currentReport.value)
    }

    if (response && (response.code === 200 || response.status === 200)) {
      ElMessage.success(response.message || response.data || (currentReport.value.id ? '更新报告成功' : '新增报告成功'))
      reportDialogVisible.value = false
      fetchActivityReports()
    } else {
      ElMessage.error(response.message || response.data || (currentReport.value.id ? '更新报告失败' : '新增报告失败'))
    }
  } catch (error) {
    console.error('保存报告失败:', error)
    ElMessage.error('保存报告失败: ' + (error.message || '网络错误'))
  }
}

// 查看报告详情（弹出框形式）
const viewReportDetail = (row) => {
  // 获取完整的报告详情
  getReportDetail(row.id);
}

// 获取报告详情
const getReportDetail = async (id) => {
  try {
    const response = await request.get('/activityReport/getById', {
      params: { id: id }
    });
    console.log('获取报告详情响应:', response);

    if (response && response.data) {
      // 检查response.data是否直接就是报告对象
      if (response.data.id) {
        detailReport.value = response.data;
        reportDetailDialogVisible.value = true;
        ElMessage.success('报告详情获取成功');
      }
      // 检查response.data中是否有data字段（后端R类封装的格式）
      else if (response.data.data && response.data.data.id) {
        detailReport.value = response.data.data;
        reportDetailDialogVisible.value = true;
        ElMessage.success('报告详情获取成功');
      } else {
        ElMessage.error('获取报告详情失败: 数据格式不正确');
      }
    } else {
      ElMessage.error('获取报告详情失败: 未收到有效数据');
    }
  } catch (error) {
    console.error('获取报告详情失败:', error);
    ElMessage.error('获取报告详情失败: ' + (error.message || '网络错误'));
  }
}

// 查看活动描述详情（参考报告内容展示方式）
const viewActivityContent = (row) => {
  detailActivity.value = { ...row }
  activityContentDialogVisible.value = true
}

// 格式化策划内容为HTML
const formatPlanContent = (content) => {
  if (!content) return '<p>暂无内容</p>';

  // 如果内容已经是HTML格式，直接返回
  if (content.trim().startsWith('<')) {
    return content;
  }

  // 如果是纯文本，转换为HTML格式
  return `<div class="plan-content-text">${content.replace(/\n/g, '<br>')}</div>`;
}

// 格式化活动内容为HTML
const formatActivityContent = (content) => {
  if (!content) return '<p>暂无内容</p>';

  // 如果内容已经是HTML格式，直接返回
  if (content.trim().startsWith('<')) {
    return content;
  }

  // 如果是纯文本，转换为HTML格式
  return `<div class="activity-content-text">${content.replace(/\n/g, '<br>')}</div>`;
}

// 格式化报告内容为HTML
const formatReportContent = (content) => {
  if (!content) return '<p>暂无内容</p>';

  // 如果内容已经是HTML格式，直接返回
  if (content.trim().startsWith('<')) {
    return content;
  }

  // 如果是纯文本，转换为HTML格式
  return `<div class="report-content-text">${content.replace(/\n/g, '<br>')}</div>`;
}

const handleDeleteReport = (row) => {
  ElMessageBox.confirm(
    `确定要删除报告 "${row.title}" 吗？此操作不可恢复。`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async () => {
      try {
        const response = await request.delete('/activityReport/delete', {
          params: { id: row.id }
        })
        if (response && (response.code === 200 || response.status === 200)) {
          ElMessage.success(response.message || response.data || '删除成功')
          fetchActivityReports()
        } else {
          ElMessage.error(response.message || response.data || '删除失败')
        }
      } catch (error) {
        console.error('删除失败:', error)
        ElMessage.error('删除失败: ' + (error.message || '网络错误'))
      }
    })
    .catch(() => {
      ElMessage.info('已取消删除')
    })
}

const onReportDialogClose = () => {
  reportDialogVisible.value = false
}

// 状态文本映射
const getPlanStatusText = (status) => {
  const statusMap = {
    0: '待审批',
    1: '已批准',
    2: '已拒绝'
  }
  return statusMap[Number(status)] || '未知状态'
}

// 根据状态名称获取标签类型
const getPlanStatusTagTypeByName = (statusName) => {
  const typeMap = {
    '待审批': 'info',    // 蓝色
    '已批准': 'success', // 绿色
    '已拒绝': 'danger'   // 红色
  }
  return typeMap[statusName] || 'info'
}

const getPlanStatusTagType = (status) => {
  const numStatus = Number(status);
  const typeMap = {
    0: 'info',    // 待审批 - 蓝色
    1: 'success', // 已批准 - 绿色
    2: 'danger'   // 已拒绝 - 红色
  }
  return typeMap[numStatus] || 'info'
}

const getActivityStatusText = (status) => {
  const statusMap = {
    1: '筹备中',
    2: '进行中',
    3: '已结束',
    4: '已取消'
  }
  return statusMap[Number(status)] || '未知状态'
}

// 获取活动状态的CSS类名
const getActivityStatusClass = (status) => {
  const classMap = {
    1: 'status-preparing',  // 筹备中
    2: 'status-processing', // 进行中
    3: 'status-finished',   // 已结束
    4: 'status-cancelled'   // 已取消
  }
  return classMap[status] || ''
}

const getReportStatusText = (status) => {
  const statusMap = {
    0: '草稿',
    1: '已完成',
    2: '已归档'
  }
  return statusMap[Number(status)] || '未知状态'
}

const getReportStatusTagType = (status) => {
  const numStatus = Number(status);
  const typeMap = {
    0: 'info',    // 草稿 - 蓝色
    1: 'success', // 已完成 - 绿色
    2: 'warning'  // 已归档 - 橙色
  }
  return typeMap[numStatus] || 'info'
}

// 初始化加载数据
onMounted(() => {
  console.log('页面初始化，开始获取活动数据')
  // 默认加载活动策划数据
  if (activeMenu.value === 'promotion') {
    fetchPromotionPlans()
  }
})
</script>

<style scoped>
.marketing-layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  width: 150px;
  color: #000;
  transition: width 0.3s ease;
  height: 100%;
  overflow-y: auto;
  z-index: 100;
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  height: 60px;
  box-sizing: border-box;
}

.sidebar-header h3 {
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.toggle-btn {
  margin-left: 10px;
}

.sidebar :deep(.el-menu) {
  border-right: none;
  height: calc(100% - 60px);
}

.sidebar :deep(.el-menu-item) {
  color: #bfcbd9;
}

.sidebar :deep(.el-menu-item:hover),
.sidebar :deep(.el-menu-item.is-active) {
  color: #409eff !important;
}

.main-content {
  flex: 1;
  padding: 20px;
  background-color: #f5f7fa;
  overflow-y: auto;
  transition: margin-left 0.3s ease;
}

.main-content.collapsed {
  margin-left: 64px;
}

/* 活动策划样式 */
.promotion-container,
.marketing-container,
.report-container {
  height: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px 0;
  background-color: #ffffff;
  border-radius: 8px;
}

.header h2 {
  margin: 0;
  color: #1a1a1a;
  font-size: 18px;
  font-weight: 600;
  padding-bottom: 0;
}

/* 搜索表单样式 */
.search-form {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 16px;
  padding: 16px;
  margin-bottom: 24px;
  background-color: #ffffff;
  border-radius: 4px;
}

.search-form .el-form-item {
  margin-bottom: 0;
  display: flex;
  align-items: center;
  white-space: nowrap;
}

.search-form .el-form-item__label {
  padding-right: 8px;
  margin-bottom: 0;
  white-space: nowrap;
  width: auto;
  text-align: left;
}

.search-form .el-input,
.search-form .el-select {
  width: 180px;
  min-width: 180px;
}

.search-form .el-form-item:last-child {
  margin-left: auto;
}

/* 表格样式 */
.plans-table,
.activities-table,
.reports-table {
  width: 100%;
  margin-top: 16px;
  border: 1px solid #f0f2f7;
  border-radius: 4px;
  overflow: hidden;
  transition: all 0.2s ease;
}

.plans-table :deep(.el-table__row),
.activities-table :deep(.el-table__row),
.reports-table :deep(.el-table__row) {
  height: 78px;
  transition: all 0.4s cubic-bezier(0.2, 0.8, 0.2, 1);
}

.plans-table :deep(.el-table__row:hover),
.activities-table :deep(.el-table__row:hover),
.reports-table :deep(.el-table__row:hover) {
  background-color: #f8fafc;
  transform: translateY(-6px);
  box-shadow: 0 14px 32px rgba(58, 87, 232, 0.28);
  z-index: 1;
  position: relative;
}

.plans-table :deep(.el-table__cell),
.activities-table :deep(.el-table__cell),
.reports-table :deep(.el-table__cell) {
  padding: 24px 0;
  border-bottom: 1px solid #f0f2f7;
  transition: all 0.2s ease;
}

.plans-table :deep(.el-table__row--striped),
.activities-table :deep(.el-table__row--striped),
.reports-table :deep(.el-table__row--striped) {
  background-color: #fafbfe;
}

.plans-table :deep(.el-table__row--striped:hover),
.activities-table :deep(.el-table__row--striped:hover),
.reports-table :deep(.el-table__row--striped:hover) {
  background-color: #f5f7ff;
}

.plans-table :deep(.el-table__header th),
.activities-table :deep(.el-table__header th),
.reports-table :deep(.el-table__header th) {
  background-color: #f8fafc;
  color: #5a6d8a;
  font-weight: 600;
  font-size: 14px;
  letter-spacing: 0.5px;
}

/* 分页样式 */
.pagination {
  margin-top: 24px;
  padding: 12px 0;
  background-color: #f8fafc;
  border-radius: 8px;
  justify-content: center;
}

.pagination .btn-prev,
.pagination .btn-next,
.pagination .number {
  min-width: 32px;
  height: 32px;
  line-height: 32px;
  border-radius: 4px;
}

.pagination .number.active {
  background-color: #3a57e8;
  color: white;
}

.pagination .number:hover:not(.active) {
  color: #3a57e8;
}

/* 按钮样式 */
.el-button {
  transition: all 0.2s ease;
  border-radius: 4px;
  font-weight: 500;
}

.search-form .el-button--primary {
  background-color: #3a57e8;
  border-color: #3a57e8;
  color: #ffffff;
  padding: 8px 16px;
}

.search-form .el-button--primary:hover {
  background-color: #2c46d8;
  border-color: #2c46d8;
}

.el-table .el-button {
  padding: 6px 12px;
  font-size: 12px;
  border-radius: 4px;
  margin-right: 5px;
}

.el-table .el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

/* 对话框样式 */
.el-dialog {
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(58, 87, 232, 0.15);
  border: none;
}

.el-dialog__header {
  padding: 18px 24px;
  border-bottom: 1px solid #f0f2f7;
  background-color: #f8fafc;
  border-radius: 12px 12px 0 0;
}

.el-dialog__title {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
}

.el-dialog__body {
  padding: 24px;
}

.el-dialog__footer {
  padding: 16px 24px;
  background-color: #f8fafc;
  border-radius: 0 0 12px 12px;
  text-align: right;
}

.el-dialog__footer .el-button {
  min-width: 80px;
  padding: 8px 16px;
}

.el-dialog__footer .el-button + .el-button {
  margin-left: 12px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
}

/* 对话框表单样式 */
.dialog-form .el-form-item,
.detail-form .el-form-item {
  margin-bottom: 18px;
}

.dialog-form .el-form-item__label,
.detail-form .el-form-item__label {
  padding-right: 16px;
  font-weight: 500;
  color: #606266;
}

/* 标签样式 */
.el-tag {
  margin: 2px;
  font-weight: 500;
}

.el-tag.el-tag--danger {
  background-color: #fef0f0;
  border-color: #fde2e2;
  color: #f56c6c;
}

.el-tag.el-tag--primary {
  background-color: #ecf5ff;
  border-color: #d9ecff;
  color: #409eff;
}

.el-tag.el-tag--success {
  background-color: #f0f9eb;
  border-color: #e1f3d8;
  color: #67c23a;
}

.el-tag.el-tag--info {
  background-color: #f4f4f5;
  border-color: #e9e9eb;
  color: #909399;
}

.el-tag.el-tag--warning {
  background-color: #fdf6ec;
  border-color: #faecd8;
  color: #e6a23c;
}

/* 活动描述详情样式（参考报告内容样式） */
.activity-content-section,
.plan-content-section,
.report-content-section {
  margin-top: 20px;
}

.activity-content-section h3,
.plan-content-section h3,
.report-content-section h3 {
  margin: 0 0 15px 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.activity-content-html,
.plan-content-html,
.report-content-html {
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
  min-height: 200px;
}

.activity-content-text,
.plan-content-text,
.report-content-text {
  line-height: 1.6;
  color: #606266;
  white-space: pre-wrap;
}

:deep(.activity-content-html h1),
:deep(.plan-content-html h1),
:deep(.report-content-html h1),
:deep(.activity-content-html h2),
:deep(.plan-content-html h2),
:deep(.report-content-html h2),
:deep(.activity-content-html h3),
:deep(.plan-content-html h3),
:deep(.report-content-html h3) {
  margin: 15px 0;
  color: #303133;
}

:deep(.activity-content-html p),
:deep(.plan-content-html p),
:deep(.report-content-html p) {
  margin: 10px 0;
  line-height: 1.6;
  color: #606266;
}

:deep(.activity-content-html ul),
:deep(.plan-content-html ul),
:deep(.report-content-html ul),
:deep(.activity-content-html ol),
:deep(.plan-content-html ol),
:deep(.report-content-html ol) {
  padding-left: 20px;
  margin: 10px 0;
}

:deep(.activity-content-html li),
:deep(.plan-content-html li),
:deep(.report-content-html li) {
  margin: 5px 0;
}

:deep(.activity-content-html strong),
:deep(.plan-content-html strong),
:deep(.report-content-html strong) {
  font-weight: 600;
}

:deep(.activity-content-html em),
:deep(.plan-content-html em),
:deep(.report-content-html em) {
  font-style: italic;
}

/* 活动状态样式 */
.status-preparing {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  background-color: #f4f4f5;
  border: 1px solid #e9e9eb;
  color: #909399;
}

.status-processing {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  background-color: #ecf5ff;
  border: 1px solid #d9ecff;
  color: #409eff;
}

.status-finished {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  background-color: #f0f9eb;
  border: 1px solid #e1f3d8;
  color: #67c23a;
}

.status-cancelled {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  background-color: #fef0f0;
  border: 1px solid #fde2e2;
  color: #f56c6c;
}

/* 二维码样式 */
.qr-code-container {
  text-align: center;
  margin-top: 10px;
}

.qr-code-row {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 20px;
}

.qr-code-item {
  flex: 1;
  min-width: 200px;
  padding: 10px;
}

.qr-code-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 160px;
}

.qr-code-image {
  width: 150px;
  height: 150px;
  border: 1px solid #ebeef5;
  padding: 5px;
  border-radius: 5px;
  margin-bottom: 5px;
  cursor: pointer;
}

.qr-code-description {
  margin: 5px 0;
  font-size: 12px;
  color: #909399;
}

.qr-code-error {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 5px;
}

.qr-code-placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 160px;
  color: #909399;
  font-size: 14px;
  padding: 20px;
  text-align: center;
  border: 1px dashed #ebeef5;
  border-radius: 5px;
}

/* 大二维码预览样式 */
.large-qr-code-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.large-qr-code-image {
  width: 450px;
  height: 450px;
  border: 1px solid #ebeef5;
  padding: 5px;
  border-radius: 5px;
}
</style>
