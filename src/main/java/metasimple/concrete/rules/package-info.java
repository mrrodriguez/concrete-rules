/**
 * The Java API for working with Concrete Rules. It contains three simple pieces:
 *
 * <ul>
 *     <li>The {@link metasimple.concrete.rules.RuleLoader RuleLoader}, responsible for loading rules into a new working memory</li>
 *     <li>The {@link metasimple.concrete.rules.WorkingMemory WorkingMemory}, an immutable instance of a rule session.</li>
 *     <li>The {@link metasimple.concrete.rules.QueryResult QueryResult}, a container of query results.</li>
 * </ul>
 *
 * <p>
 * Note this API does not have a separate "knowledge base" class like those of other rules engines. Instead,
 * the user can simply create and reuse a single, empty WorkingMemory object for multiple rule instances -- optionally
 * sticking the initial empty working memory in a static variable. This type of pattern is efficient and possible
 * since the WorkingMemory is immutable, creating a new instance that shares internal state when changes occur.
 * </p>
 *
 */
package metasimple.concrete.rules;
