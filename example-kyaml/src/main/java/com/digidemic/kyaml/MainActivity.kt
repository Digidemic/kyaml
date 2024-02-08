/**
 * Kyaml v1.0.0 - https://github.com/Digidemic/Kyaml
 * (c) 2024 DIGIDEMIC, LLC - All Rights Reserved
 * Kyaml developed by Adam Steinberg of DIGIDEMIC, LLC
 * License: Apache License 2.0
 *
 * ====
 *
 * Copyright 2024 DIGIDEMIC, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.digidemic.kyaml

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.digidemic.kyaml.ui.theme.KyamlTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KyamlTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column{
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .fillMaxWidth()
                        ) {
                            SectionTitle("Kyaml Demo App")
                            SectionSubtitle("Tap a button to run the YAML file with Kyaml.")
                            SectionSubtitle("Check Logcat for \"KYAML\" logs for the results.")
                            SectionSubtitle("Make custom changes to any YAML file in /assets or /res/raw, rebuild the app, then observe the changes!")
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        KyamlButton("Each Variable Type", R.raw.each_variable_type)
                        KyamlButton("String Variations", R.raw.string_variations)
                        KyamlButton("Block Scalar", R.raw.block_scalar)
                        KyamlButton("Flow Sequences", "flow_sequences.yaml")
                        KyamlButton("Flow Dictionary", "flow_dictionary.yaml")
                        KyamlButton("Block Sequences", "block_sequences.yaml")
                        KyamlButton("Block Dictionary", "block_dictionary.yaml")
                        KyamlButton("Invalid Asset File", "this_file_does_not_exist.yaml")
                    }
                }
            }
        }
    }

    @Composable
    fun KyamlButton(
        buttonTitle: String,
        yamlFileNameInAssetsDirectory: String,
        onEachItem: ((String, Any?) -> Unit)? = null
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
            Kyaml(
                activity = this@MainActivity,
                yamlFileNameInAssets = yamlFileNameInAssetsDirectory,
                onEachItem = { key, value ->
                    Log.i("KYAML", "$key | $value | ${getValueType(value)}")
                    onEachItem?.invoke(key, value)
                },
                onError =  {
                    onError(it)
                }
            )
        }) {
            Text(text = buttonTitle)
        }
    }

    @Composable
    fun KyamlButton(
        buttonTitle: String,
        @RawRes yamlRawFileResourceId: Int,
        onEachItem: ((String, Any?) -> Unit)? = null
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
            Kyaml(
                activity = this@MainActivity,
                yamlResourceIdInRaw = yamlRawFileResourceId,
                onEachItem = { key, value ->
                    Log.i("KYAML", "$key | $value | ${getValueType(value)}")
                    onEachItem?.invoke(key, value)
                },
                onError =  {
                    onError(it)
                }
            )
        }) {
            Text(text = buttonTitle)
        }
    }

    @Composable
    fun SectionTitle(text: String) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(4.dp)
        )
    }

    @Composable
    fun SectionSubtitle(text: String) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(4.dp)
        )
    }

    private fun onError(e: Exception) {
        Log.w("KYAML", e)
    }

    private fun getValueType(value: Any?): String {
        val mainType = value?.let {
            it::class.simpleName
        } ?: "null"

        var listType = ""
        (value as? List<*>)?.let {  lst ->
            for(item in lst) {
                if(item != null) {
                    listType = " of ${item::class.simpleName}"
                    break
                }
            }
        }

        return mainType + listType
    }
}