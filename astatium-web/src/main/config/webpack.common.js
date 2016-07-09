/**
 * Copyright 2016 Phillip DuLion
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var helpers = require('./helpers');

module.exports = {
	entry : {
		'polyfills' : './src/main/angular/polyfills.ts',
		'vendor' : './src/main/angular/vendor.ts',
		'app' : './src/main/angular/main.ts'
	},

	resolve : {
		extensions : [ '', '.js', '.ts' ]
	},

	module : {
		loaders : [ {
			test : /\.ts$/,
			loaders : [ 'ts', 'angular2-template-loader' ]
		}, {
			test : /\.html$/,
			loader : 'html'
		}, {
			test : /\.(png|jpe?g|gif|svg|woff|woff2|ttf|eot|ico)$/,
			loader : 'file?name=assets/[name].[hash].[ext]'
		}, {
			test : /\.css$/,
			exclude : helpers.root('src', 'main', 'angular'),
			loader : ExtractTextPlugin.extract('style', 'css?sourceMap')
		}, {
			test : /\.css$/,
			include : helpers.root('src', 'main', 'angular'),
			loader : 'raw'
		} ]
	},

	plugins : [ new webpack.optimize.CommonsChunkPlugin({
		name : [ 'app', 'vendor', 'polyfills' ]
	}),

	new HtmlWebpackPlugin({
		template : 'src/main/angular/index.html'
	}) ]
};