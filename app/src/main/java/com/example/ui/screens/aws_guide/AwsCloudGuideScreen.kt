package com.example.ui.screens.aws_guide

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.WorkerConnectViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AwsCloudGuideScreen(
    viewModel: WorkerConnectViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    var selectedSection by remember { mutableStateOf(0) } // 0: Overview, 1: DynamoDB, 2: Cognito & S3, 3: Checklist & Costs

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AWS Cloud Architecture & Blueprint", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("back_from_aws_btn")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Section Switcher TabRow
            TabRow(
                selectedTabIndex = selectedSection,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = PrimaryTeal
            ) {
                Tab(
                    selected = selectedSection == 0,
                    onClick = { selectedSection = 0 },
                    text = { Text("Architecture", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedSection == 1,
                    onClick = { selectedSection = 1 },
                    text = { Text("DynamoDB", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedSection == 2,
                    onClick = { selectedSection = 2 },
                    text = { Text("Cognito & S3", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedSection == 3,
                    onClick = { selectedSection = 3 },
                    text = { Text("Setup & Costs", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                when (selectedSection) {
                    0 -> {
                        // Section 0: High Level Architecture
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = DeepNavyDark)
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Text(
                                        text = "Worker Connect AWS Cloud Topology (ap-south-1 Mumbai)",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "1. Client Apps: Native Android & React Web powered by REST endpoints\n" +
                                                "2. Auth: Amazon Cognito User Pool with Customer & Worker Groups\n" +
                                                "3. API Layer: Amazon API Gateway (HTTP APIs) with JWT Cognito Authorizer\n" +
                                                "4. Compute: AWS Lambda (Node.js/Python microservices)\n" +
                                                "5. Database: Amazon DynamoDB (Single-table or multi-table on-demand)\n" +
                                                "6. KYC Storage: Private Amazon S3 Bucket with short-lived presigned URLs\n" +
                                                "7. Transactional Alerts: Amazon SNS (SMS OTP) + Amazon SES (Email)",
                                        color = Color(0xFFCBD5E1),
                                        fontSize = 12.sp,
                                        lineHeight = 20.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }

                        item {
                            ArchitectureStepCard(
                                title = "1. Security & Privacy by Design",
                                description = "Worker personal documents (Aadhaar, Voter ID, Wireman licenses) are never stored in public folders or transmitted over unauthenticated connections. Uploads use AWS S3 Pre-Signed URLs valid for 15 minutes only."
                            )
                        }

                        item {
                            ArchitectureStepCard(
                                title = "2. Indian Localization & Low Latency",
                                description = "Primary AWS region: ap-south-1 (Mumbai). Edge caching via CloudFront ensures sub-50ms latency across Tamil Nadu (Chennai, Coimbatore, Madurai, Salem, Tiruchirappalli)."
                            )
                        }
                    }

                    1 -> {
                        // Section 1: DynamoDB Schema
                        item {
                            Text(
                                text = "Production DynamoDB Access Patterns & Schemas",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        item {
                            SchemaTableCard(
                                tableName = "WorkerConnect_Workers",
                                partitionKey = "PK: WORKER#<workerId>",
                                sortKey = "SK: PROFILE",
                                gsi = "GSI1: CityCategoryIndex (PK: CITY#<city>, SK: CAT#<categoryId>#RATING#<rating>)",
                                attributes = "fullName, displayName, city, locality, pinCode, visitCharge, minLabourCharge, rating, reviewCount, availabilityStatus, verificationStatus, badges, phone"
                            )
                        }

                        item {
                            SchemaTableCard(
                                tableName = "WorkerConnect_ServiceRequests",
                                partitionKey = "PK: REQ#<requestId>",
                                sortKey = "SK: METADATA",
                                gsi = "GSI1: CustomerRequests (PK: CUST#<customerId>, SK: CREATED#<timestamp>)\nGSI2: WorkerRequests (PK: WORKER#<workerId>, SK: STATUS#<status>)",
                                attributes = "customerId, workerId, description, preferredSlot, urgency, status, statusHistory, isReviewed"
                            )
                        }

                        item {
                            SchemaTableCard(
                                tableName = "WorkerConnect_Reviews",
                                partitionKey = "PK: WORKER#<workerId>",
                                sortKey = "SK: REV#<requestId>",
                                gsi = "None (Queried directly by Worker PK with ScanIndexForward = false)",
                                attributes = "rating (1-5), comment, customerName, date, verifiedBookingFlag"
                            )
                        }
                    }

                    2 -> {
                        // Section 2: Cognito & S3 Setup
                        item {
                            Text(
                                text = "Cognito User Pool & Private S3 KYC Configuration",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Amazon Cognito User Pool Settings", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "• Pool Name: worker-connect-users\n" +
                                                "• Sign-in attributes: Email address and verified Phone number\n" +
                                                "• Password Policy: Min 8 chars, 1 uppercase, 1 lowercase, 1 number\n" +
                                                "• Custom Attributes: custom:role (customer | worker | admin)\n" +
                                                "• App Client: Public client (No client secret for mobile & web apps)",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Private S3 KYC Document Bucket", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "• Bucket Name: worker-connect-kyc-private-tn\n" +
                                                "• Block all public access: ENABLED (Strict Zero-Trust)\n" +
                                                "• Server-Side Encryption: AES-256 (SSE-S3)\n" +
                                                "• CORS: Restricted to app origins only\n" +
                                                "• Access: Generated Lambda Pre-signed PUT/GET URLs only",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    3 -> {
                        // Section 3: Beginner Setup Checklist & Cost Control
                        item {
                            Text(
                                text = "Zero-Cost Free Tier Setup & Budget Protection",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = AccentGreenLight)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "AWS Free Tier Coverage for Worker Connect",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFF065F46)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "• Amazon DynamoDB: 25 GB of storage & 25 Read/Write Capacity Units FREE forever.\n" +
                                                "• AWS Lambda: 1,000,000 requests per month FREE forever.\n" +
                                                "• Amazon Cognito: 50,000 Monthly Active Users (MAUs) FREE forever.\n" +
                                                "• Amazon S3: 5 GB standard storage FREE for 12 months.\n" +
                                                "• Expected First Year Running Cost: ₹0.00 / $0.00 if operated on Free Tier.",
                                        fontSize = 12.sp,
                                        color = Color(0xFF065F46),
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Essential AWS Budget Alert Setup (Prevent Unexpected Bills)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "1. Open AWS Console -> Search 'AWS Budgets'\n" +
                                                "2. Click 'Create Budget' -> Choose 'Zero Spend Budget'\n" +
                                                "3. Enter your alert email address\n" +
                                                "4. If any service exceeds $0.01, AWS immediately emails you an alert.\n" +
                                                "5. Ensure DynamoDB tables are created with 'On-Demand Capacity' or 5 RCU/WCU to stay within the 25 RCU free threshold.",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArchitectureStepCard(title: String, description: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
fun SchemaTableCard(
    tableName: String,
    partitionKey: String,
    sortKey: String,
    gsi: String,
    attributes: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Table: $tableName", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PrimaryTeal)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = partitionKey, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
            Text(text = sortKey, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Index: $gsi", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Attributes: $attributes", fontSize = 11.sp, color = Color.Gray)
        }
    }
}
