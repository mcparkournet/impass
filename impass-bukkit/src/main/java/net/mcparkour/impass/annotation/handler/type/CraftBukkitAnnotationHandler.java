/*
 * MIT License
 *
 * Copyright (c) 2019 MCParkour
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.mcparkour.impass.annotation.handler.type;

import net.mcparkour.common.reflection.Reflections;
import net.mcparkour.impass.annotation.type.CraftBukkitType;
import net.mcparkour.impass.handler.type.TypeAnnotationHandler;

public class CraftBukkitAnnotationHandler implements TypeAnnotationHandler<CraftBukkitType> {

	private String serverVersion;

	public CraftBukkitAnnotationHandler(String serverVersion) {
		this.serverVersion = serverVersion;
	}

	@Override
	public Class<?> handle(CraftBukkitType annotation) {
		var className = annotation.value();
		return Reflections.getClass("org.bukkit.craftbukkit." + this.serverVersion + "." + className);
	}

	@Override
	public Class<CraftBukkitType> getAnnotationType() {
		return CraftBukkitType.class;
	}
}
