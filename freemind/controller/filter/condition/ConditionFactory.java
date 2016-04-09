/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*
 * Created on 17.05.2005
 *
 */
package freemind.controller.filter.condition;

import javax.swing.JComponent;
import javax.swing.JLabel;

import freemind.common.NamedObject;
import freemind.main.Resources;
import freemind.main.XMLElement;

/**
 * @author dimitri 17.05.2005
 */
public class ConditionFactory {
	
	static final NamedObject FILTER_NODE= Resources.getInstance().createTranslatedString("filter_node");
	static final NamedObject FILTER_ICON = Resources.getInstance().createTranslatedString("filter_icon");

	static final NamedObject FILTER_CONTAINS = Resources.getInstance().createTranslatedString("filter_contains");
	static final NamedObject FILTER_NOT_CONTAINS = Resources.getInstance().createTranslatedString("filter_not_contains") ;
	static final NamedObject FILTER_IS_NOT_EQUAL_TO = Resources.getInstance().createTranslatedString("filter_is_not_equal_to") ;
	static final NamedObject FILTER_IS_EQUAL_TO =Resources.getInstance().createTranslatedString("filter_is_equal_to") ;
	static final NamedObject FILTER_LE = NamedObject.literal("<=");
	static final NamedObject FILTER_LT = NamedObject.literal("<");
	static final NamedObject FILTER_GE = NamedObject.literal(">=");
	static final NamedObject FILTER_GT = NamedObject.literal(">");
	
	static final String FILTER_IGNORE_CASE = "filter_ignore_case";
	
	
	
	


	/**
     *
     */
	public ConditionFactory() {
	}

	static String createDescription(String attribute, String simpleCondition,
			String value, boolean ignoreCase) {
		String description = attribute
				+ " "
				+ simpleCondition
				+ (value != null ? " \"" + value + "\"" : "")
				+ (ignoreCase && value != null ? ", "
						+ Resources.getInstance().getResourceString(
								FILTER_IGNORE_CASE) : "");
		return description;
	}

	public Condition loadCondition(XMLElement element) {
		if (element.getName().equalsIgnoreCase(NodeContainsCondition.NAME))
			return NodeContainsCondition.load(element);
		if (element.getName().equalsIgnoreCase(
				IgnoreCaseNodeContainsCondition.NAME))
			return IgnoreCaseNodeContainsCondition.load(element);
		if (element.getName().equalsIgnoreCase(NodeCompareCondition.NAME))
			return NodeCompareCondition.load(element);
		if (element.getName().equalsIgnoreCase(IconContainedCondition.NAME))
			return IconContainedCondition.load(element);
       	if (element.getName().equalsIgnoreCase(IconNotContainedCondition.NAME))
			return IconNotContainedCondition.load(element);
		if (element.getName().equalsIgnoreCase(
				ConditionNotSatisfiedDecorator.NAME)) {
			return ConditionNotSatisfiedDecorator.load(element);
		}
		if (element.getName().equalsIgnoreCase(ConjunctConditions.NAME)) {
			return ConjunctConditions.load(element);
		}
		if (element.getName().equalsIgnoreCase(DisjunctConditions.NAME)) {
			return DisjunctConditions.load(element);
		}
		return null;
	}

	public Condition createCondition(NamedObject attribute,
			NamedObject simpleCondition, String value, boolean ignoreCase) {
		if (attribute.equals(FILTER_ICON)
				&& simpleCondition.equals(FILTER_CONTAINS))
			return new IconContainedCondition(value);
        if (attribute.equals(FILTER_ICON)
            && simpleCondition.equals(FILTER_NOT_CONTAINS)    )
            return new IconNotContainedCondition(value);
		if (attribute.equals(FILTER_NODE)) {
			return createNodeCondition(simpleCondition, value, ignoreCase);
		}
		return null;
	}

	public NamedObject[] getNodeConditionNames() {
		return new NamedObject[] {
				FILTER_CONTAINS, FILTER_IS_EQUAL_TO, FILTER_IS_NOT_EQUAL_TO,
				FILTER_GT, FILTER_GE, FILTER_LE, FILTER_LT };
	}

	public Object[] getIconConditionNames() {
		return new NamedObject[] { FILTER_CONTAINS, FILTER_NOT_CONTAINS};
	}

	public NamedObject[] getAttributeConditionNames() {
		return new NamedObject[] {
				FILTER_NODE, FILTER_ICON };
	}
	protected Condition createNodeCondition(NamedObject simpleCondition,
			String value, boolean ignoreCase) {
		if (ignoreCase) {
			if (simpleCondition.equals(FILTER_CONTAINS)) {
				if (value.equals(""))
					return null;
				return new IgnoreCaseNodeContainsCondition(value);
			}
			if (simpleCondition.equals(FILTER_IS_EQUAL_TO))
				return new NodeCompareCondition(value, true, 0, true);
			if (simpleCondition.equals(FILTER_IS_NOT_EQUAL_TO))
				return new NodeCompareCondition(value, true, 0, false);
			if (simpleCondition.equals(FILTER_GT))
				return new NodeCompareCondition(value, true, 1, true);
			if (simpleCondition.equals(FILTER_GE))
				return new NodeCompareCondition(value, true, -1, false);
			if (simpleCondition.equals(FILTER_LT))
				return new NodeCompareCondition(value, true, -1, true);
			if (simpleCondition.equals(FILTER_LE))
				return new NodeCompareCondition(value, true, 1, false);
		} else {
			if (simpleCondition.equals(FILTER_CONTAINS)) {
				if (value.equals(""))
					return null;
				return new NodeContainsCondition(value);
			}
			if (simpleCondition.equals(FILTER_IS_EQUAL_TO))
				return new NodeCompareCondition(value, false, 0, true);
			if (simpleCondition.equals(FILTER_IS_NOT_EQUAL_TO))
				return new NodeCompareCondition(value, false, 0, false);
			if (simpleCondition.equals(FILTER_GT))
				return new NodeCompareCondition(value, false, 1, true);
			if (simpleCondition.equals(FILTER_GE))
				return new NodeCompareCondition(value, false, -1, false);
			if (simpleCondition.equals(FILTER_LT))
				return new NodeCompareCondition(value, false, -1, true);
			if (simpleCondition.equals(FILTER_LE))
				return new NodeCompareCondition(value, false, 1, false);
		}
		return null;
	}

	static public JComponent createCellRendererComponent(String description) {
		JCondition component = new JCondition();
		JLabel label = new JLabel(description);
		component.add(label);
		return component;
	}

}