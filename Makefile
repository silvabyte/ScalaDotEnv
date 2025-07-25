# DotEnv

# ============================================================================
# BUILD & COMPILATION
# ============================================================================
#
# Compile all modules 
.PHONY: compile
compile:
	@echo "🔨 Compiling DotEnv..."
	./mill DotEnv.compile

# Create executable JAR assembly
.PHONY: assembly
assembly:
	@echo "📦 Creating assembly JAR..."
	./mill DotEnv.assembly

# Clean build artifacts
.PHONY: clean-build
clean-build:
	@echo "🧹 Cleaning build artifacts..."
	./mill clean
	rm -rf out/

# Deep clean - removes all build artifacts including IDE files
.PHONY: clean
clean: clean-build
	@echo "🧹 Deep cleaning..."
	rm -rf .bloop .bsp .metals out target
	@echo "✅ Deep clean completed!"

# ============================================================================
# TESTING
# ============================================================================

# Run all tests for the project
.PHONY: test
test:
	@echo "🧪 Running all tests..."
	./mill DotEnv.test

.PHONY: format
format:
	@echo "🎨 Code formatting..."
	./mill mill.scalalib.scalafmt.ScalafmtModule/reformatAll
	
# Show version information
.PHONY: version
version:
	@echo "📋 Version Information:"
	@echo "  DotEnv: $$(cat package.json | jq '.version')"
	@echo "  Scala: $$(./mill DotEnv.scalaVersion)"
	@echo "  Mill: $$(./mill --version 2>/dev/null || echo 'Unknown')"
	@echo "  Java: $$(java -version 2>&1 | head -1)"

# Show project directory tree
.PHONY: tree
tree:
	@echo "📁 Project Structure:"
	@tree -I 'out|.git|.metals|.bloop|.bsp|target|node_modules' -L 3 || \
	 find . -type d -name "out" -prune -o -type d -name ".git" -prune -o -type d -print | head -20
