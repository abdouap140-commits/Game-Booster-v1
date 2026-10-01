package com.example.gamebooster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val specs = getDeviceSpecs(this)
                DeviceInfoScreen(specs)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceInfoScreen(specs: DeviceSpecs) {
    val infoList = listOf(
        "الشركة المصنعة" to specs.manufacturer,
        "اسم الجهاز" to specs.deviceName,
        "الرام الكاملة" to specs.totalRam,
        "الرام الحرة" to specs.availableRam,
        "عدد الأنوية" to "${specs.cpuCores} نواة",
        "معمارية المعالج" to specs.cpuArch,
        "المساحة الكاملة" to specs.totalStorage,
        "المساحة الحرة" to specs.availableStorage,
        "مستوى البطارية" to "${specs.batteryLevel}%",
        "حرارة البطارية" to specs.batteryTemp,
        "كرت الرسوميات (GPU)" to specs.gpuRenderer
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("معلومات النظام", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(infoList) { (label, value) ->
                InfoCard(label, value)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun InfoCard(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text = value, fontSize = 16.sp, color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
        }
    }
}
