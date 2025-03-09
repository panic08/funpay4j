/*
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

package ru.funpay4j.core.commands.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import org.jetbrains.annotations.Nullable;

import ru.funpay4j.core.objects.transaction.TransactionType;

/**
 * Use this command to get transactions
 *
 * @author panic08
 * @since 1.0.6
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetTransactions {
    @NonNull private Long userId;

    @Nullable private TransactionType type;

    @NonNull private Integer pages;
}
