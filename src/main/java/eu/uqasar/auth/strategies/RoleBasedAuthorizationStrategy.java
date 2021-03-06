/*
 */
package eu.uqasar.auth.strategies;

/*
 * #%L
 * U-QASAR
 * %%
 * Copyright (C) 2012 - 2015 U-QASAR Consortium
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import eu.uqasar.auth.strategies.annotation.AnnotationBasedAuthorizationStrategy;
import eu.uqasar.auth.strategies.metadata.MetaDataAuthorizationStrategy;
import org.apache.wicket.authorization.strategies.CompoundAuthorizationStrategy;

/**
 *
 *
 */
class RoleBasedAuthorizationStrategy extends CompoundAuthorizationStrategy {

	public RoleBasedAuthorizationStrategy(final IRoleCheckingStrategy checkingStrategy) {
		add(new AnnotationBasedAuthorizationStrategy(checkingStrategy));
		add(new MetaDataAuthorizationStrategy(checkingStrategy));
	}
}
