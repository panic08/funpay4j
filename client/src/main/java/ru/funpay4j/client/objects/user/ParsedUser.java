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

package ru.funpay4j.client.objects.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

/**
 * This object represents the parsed FunPay user
 *
 * @author panic08
 * @since 1.0.6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ParsedUser {
    private long id;

    private String username;

    @Nullable
    private String avatarPhotoLink;

    private boolean isOnline;

    private List<String> badges;

    @Nullable
    private Date lastSeenAt;

    private Date registeredAt;
}
